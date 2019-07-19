package net.ramify.model.person.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.Period;

@XmlRootElement(namespace = XmlPerson.NAMESPACE, name = "exactAge")
public class XmlExactAge extends XmlAge {

    @XmlAttribute(name = "years")
    private int years;

    @XmlAttribute(name = "months")
    private int months;

    @XmlAttribute(name = "days")
    private int days;

    @Override
    public Period age() {
        return Period.of(years, months, days);
    }

}
