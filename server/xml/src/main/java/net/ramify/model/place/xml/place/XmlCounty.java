package net.ramify.model.place.xml.place;

import com.google.common.base.MoreObjects;
import net.ramify.model.ParserContext;
import net.ramify.model.place.Place;
import net.ramify.model.place.PlaceGroupId;
import net.ramify.model.place.PlaceId;
import net.ramify.model.place.id.Spid;
import net.ramify.model.place.region.County;
import net.ramify.model.place.xml.place.district.XmlMetropolitanBorough;
import net.ramify.model.place.xml.place.district.XmlRuralDistrict;
import net.ramify.model.place.xml.place.district.XmlUrbanDistrict;
import net.ramify.model.place.xml.place.manor.XmlManor;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@XmlRootElement(namespace = XmlPlace.NAMESPACE, name = "county")
class XmlCounty extends XmlPlace {

    @XmlElementRefs({
            @XmlElementRef(type = XmlParish.class),
            @XmlElementRef(type = XmlManor.class),
            @XmlElementRef(type = XmlHundred.class),
            @XmlElementRef(type = XmlWapentake.class),
            @XmlElementRef(type = XmlMetropolitanBorough.class),
            @XmlElementRef(type = XmlUrbanDistrict.class),
            @XmlElementRef(type = XmlRuralDistrict.class)
    })
    private List<XmlPlace> children;

    @XmlAttribute(name = "iso")
    private String iso;

    @Override
    protected PlaceId placeId(final String id) {
        return new Spid(County.class, id);
    }

    @Override
    protected County place(final Place parent, final PlaceGroupId groupId, final ParserContext context) throws Place.InvalidPlaceTypeException {
        Objects.requireNonNull(parent, "parent");
        return new County(this.placeId(), this.name(), parent, iso, groupId);
    }

    @Override
    protected Collection<XmlPlace> children() {
        return MoreObjects.firstNonNull(children, Collections.emptyList());
    }

}
