package net.ramify.model.place.xml.settlements;

import net.ramify.model.date.DateRange;
import net.ramify.model.date.parse.DateParser;
import net.ramify.model.date.xml.XmlBeforeDate;
import net.ramify.model.date.xml.XmlDateRange;
import net.ramify.model.place.HasPlaceId;
import net.ramify.model.place.PlaceId;
import net.ramify.model.place.church.Church;
import net.ramify.model.place.provider.PlaceProvider;
import net.ramify.model.place.type.Building;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

class XmlChurch implements HasPlaceId {

    @XmlAttribute(name = "id", required = true)
    private String placeId;

    @XmlAttribute(name = "parentId", required = true)
    private String parentId;

    @XmlAttribute(name = "denomination")
    private String denomination;

    @XmlElements({
            @XmlElement(name = "foundedBefore", type = XmlBeforeDate.class)
    })
    private XmlDateRange founded;

    @Override
    public PlaceId placeId() {
        throw new UnsupportedOperationException(); //TODO
    }

    String denomination() {
        return denomination;
    }

    @Nonnull
    DateRange founded(final DateParser dateParser) {
        return founded.resolve(dateParser);
    }

    @CheckForNull
    DateRange closed() {
        return null;
    }

    @Nonnull
    Church resolve(final PlaceProvider<Building> placeProvider, final DateParser dateParser) {
        final var place = placeProvider.require(this.placeId());
        return new ResolvedChurch(this, place, dateParser);
    }

}
