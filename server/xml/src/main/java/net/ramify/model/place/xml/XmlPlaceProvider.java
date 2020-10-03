package net.ramify.model.place.xml;

import com.google.common.base.Preconditions;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import net.ramify.model.ParserContext;
import net.ramify.model.place.Place;
import net.ramify.model.place.PlaceGroupId;
import net.ramify.model.place.PlaceId;
import net.ramify.model.place.iso.CountryIso;
import net.ramify.model.place.provider.PlaceProvider;
import net.ramify.model.place.region.Country;
import net.ramify.model.place.xml.place.XmlPlace;
import net.ramify.model.place.xml.place.XmlPlaces;
import net.ramify.utils.collections.SetUtils;
import net.ramify.utils.file.FileTraverseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlTransient;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

@XmlTransient
class XmlPlaceProvider implements PlaceProvider {

    private static final Logger logger = LoggerFactory.getLogger(XmlPlaceProvider.class);

    private final Map<PlaceId, Place> places;
    private final SetMultimap<PlaceGroupId, PlaceId> groupIds;
    private final Set<Country> countries;

    private XmlPlaceProvider(
            final Map<PlaceId, Place> places,
            final SetMultimap<PlaceGroupId, PlaceId> groupIds, final Set<Country> countries) {
        this.places = places;
        this.groupIds = groupIds;
        this.countries = countries;
    }

    @CheckForNull
    @Override
    public Place get(final PlaceId id) {
        return places.get(id);
    }

    @Nonnull
    @Override
    public Set<Country> countries(final boolean onlyTopLevel) {
        if (onlyTopLevel) return Sets.filter(countries, country -> country.parent() == null);
        return countries;
    }

    @Nonnull
    @Override
    public Set<Place> findByGroup(final PlaceGroupId groupId) {
        return SetUtils.transform(groupIds.get(groupId), this::require);
    }

    void addAll(final PlaceParserContext context, final Collection<XmlPlace> places) {
        for (final var place : places) {
            place.places(context).forEach(this::add);
        }
    }

    void add(final Place place) {
        places.put(place.placeId(), place);
        //Consumers.ifNonNull(place.parent(), parent -> children.put(parent.placeId(), place.placeId()));
        place.as(Country.class).ifPresent(countries::add);
        groupIds.put(place.placeGroupId(), place.placeId());
    }

    int size() {
        return places.size();
    }

    PlaceProvider immutable() {
        return new XmlPlaceProvider(
                ImmutableMap.copyOf(places),
                ImmutableSetMultimap.copyOf(groupIds),
                ImmutableSet.copyOf(countries));
    }

    static PlaceProvider readPlacesInCountryRoot(
            final JAXBContext jaxbContext,
            final File countryRoot,
            final ParserContext context) throws JAXBException {
        final var provider = new XmlPlaceProvider(Maps.newHashMap(), HashMultimap.create(), Sets.newHashSet());
        final var unmarshaller = jaxbContext.createUnmarshaller();
        for (final File dir : countryRoots(countryRoot)) {
            final var countryIso = CountryIso.valueOf(dir.getName());
            final var placeContext = new PlaceParserContext(context.nameParser(), context.dateParser(), countryIso, provider);
            FileTraverseUtils.traverseSubdirectories(dir, XmlPlaceProvider::includeFile, file -> readPlacesInFile(unmarshaller, file, provider, placeContext));
            logger.info("Loaded {} places from {}.", provider.size(), dir);
        }
        return provider.immutable();
    }

    private static File[] countryRoots(final File root) {
        final var roots = root.listFiles(File::isDirectory);
        Arrays.sort(roots);
        return roots;
    }

    private static boolean includeFile(final File file) {
        if (!file.getName().endsWith(".xml")) return false;
        final var path = file.getPath();
        return !path.contains("_records")
                && !path.contains("_events");
    }

    private static void readPlacesInFile(final Unmarshaller unmarshaller, final File file, final XmlPlaceProvider placeProvider, final PlaceParserContext context) {
        Preconditions.checkArgument(file.isFile(), "Not a file: %s", file);
        Preconditions.checkArgument(file.canRead(), "Not a readable file: %s", file);
        Preconditions.checkArgument(file.getName().endsWith(".xml"), "Not an XML file: %s", file);
        try {
            logger.info("Reading places from file {}", file);
            final var unmarshalled = unmarshaller.unmarshal(file);
            if (!(unmarshalled instanceof XmlPlaces)) return;
            final var places = (XmlPlaces) unmarshalled;
            placeProvider.addAll(context, places.places());
        } catch (final JAXBException jex) {
            logger.warn("Could not read places in file " + file + ": " + jex.getMessage());
        } catch (final RuntimeException rex) {
            throw new RuntimeException("Error reading places in file: " + file, rex);
        }
    }

}
