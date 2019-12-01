package net.ramify.model.place.xml.place.settlement;

import com.google.common.base.MoreObjects;
import net.ramify.model.ParserContext;
import net.ramify.model.place.Place;
import net.ramify.model.place.PlaceGroupId;
import net.ramify.model.place.PlaceHistory;
import net.ramify.model.place.PlaceId;
import net.ramify.model.place.id.Spid;
import net.ramify.model.place.settlement.City;
import net.ramify.model.place.type.Region;
import net.ramify.model.place.xml.place.XmlPlace;
import net.ramify.model.place.xml.place.building.XmlChurch;
import net.ramify.model.place.xml.place.building.XmlGraveyard;
import net.ramify.model.place.xml.place.building.XmlInn;
import net.ramify.model.place.xml.place.building.XmlMill;
import net.ramify.model.place.xml.place.building.XmlMonastery;
import net.ramify.model.place.xml.place.building.XmlSchool;
import net.ramify.model.place.xml.place.building.XmlStreet;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlRootElement(namespace = XmlPlace.NAMESPACE, name = "city")
public class XmlCity extends XmlPlace {

    @XmlElementRefs({
            @XmlElementRef(type = XmlStreet.class),
            @XmlElementRef(type = XmlChurch.class),
            @XmlElementRef(type = XmlMonastery.class),
            @XmlElementRef(type = XmlMill.class),
            @XmlElementRef(type = XmlSchool.class),
            @XmlElementRef(type = XmlInn.class),
            @XmlElementRef(type = XmlGraveyard.class)
    })
    private List<XmlPlace> children;

    @Override
    protected PlaceId placeId(final String id) {
        return new Spid(City.class, id);
    }

    @Override
    protected City place(final Place parent, final PlaceGroupId groupId, final PlaceHistory history, final ParserContext context) throws Place.InvalidPlaceTypeException {
        Objects.requireNonNull(parent, "parent");
        return new City(this.placeId(), this.name(), parent.requireAs(Region.class), groupId, history);
    }

    @Override
    protected Collection<XmlPlace> children() {
        return MoreObjects.firstNonNull(children, Collections.emptyList());
    }

}
