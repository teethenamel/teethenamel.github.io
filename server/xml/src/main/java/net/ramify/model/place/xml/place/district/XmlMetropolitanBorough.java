package net.ramify.model.place.xml.place.district;

import net.ramify.model.place.Place;
import net.ramify.model.place.region.district.MetropolitanBorough;
import net.ramify.model.place.xml.place.XmlArea;
import net.ramify.model.place.xml.place.XmlPlace;
import net.ramify.model.place.xml.place.settlement.XmlCity;
import net.ramify.model.place.xml.place.settlement.XmlTown;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;
import java.util.List;

@XmlRootElement(namespace = XmlPlace.NAMESPACE, name = "metropolitanBorough")
public class XmlMetropolitanBorough extends XmlArea<MetropolitanBorough> {

    @XmlElementRefs({
            @XmlElementRef(type = XmlCivilParish.class),
            @XmlElementRef(type = XmlCity.class),
            @XmlElementRef(type = XmlTown.class),
    })
    private List<XmlPlace> children;

    XmlMetropolitanBorough() {
        super(MetropolitanBorough.class);
    }

    @Override
    protected MetropolitanBorough place(final Place parent) throws Place.InvalidPlaceTypeException {
        return new MetropolitanBorough(this.placeId(), this.name(), parent);
    }

    @Override
    protected Collection<XmlPlace> children() {
        return children;
    }

}
