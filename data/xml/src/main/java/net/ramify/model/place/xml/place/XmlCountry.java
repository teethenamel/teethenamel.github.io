package net.ramify.model.place.xml.place;

import net.ramify.model.place.Place;
import net.ramify.model.place.PlaceId;
import net.ramify.model.place.id.Spid;
import net.ramify.model.place.region.Country;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@XmlRootElement(namespace = XmlPlace.NAMESPACE, name = "country")
class XmlCountry extends XmlPlace {

    @XmlElements({
            @XmlElement(name = "county", type = XmlCountryCounty.class),
            @XmlElement(name = "state", type = XmlState.class)
    })
    private List<XmlPlace> regions;

    @Nonnull
    PlaceId placeId(final String id) {
        return new Spid(Country.class, id);
    }

    @Override
    Place place(final PlaceId id, final String name, final Place parent) {
        return new Country(id, name);
    }

    @Override
    void addPlaces(final Set<PlaceId> places) {
        places.add(this.placeId());
        regions.forEach(region -> region.addPlaces(places));
    }

}
