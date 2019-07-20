package net.ramify.model.record.xml.record.residence;

import com.google.common.collect.Sets;
import net.ramify.model.date.DateRange;
import net.ramify.model.event.Event;
import net.ramify.model.event.type.residence.GenericResidence;
import net.ramify.model.family.Family;
import net.ramify.model.family.FamilyBuilder;
import net.ramify.model.family.xml.XmlRelationship;
import net.ramify.model.person.Person;
import net.ramify.model.person.PersonId;
import net.ramify.model.place.Place;
import net.ramify.model.place.PlaceId;
import net.ramify.model.record.GenericRecordPerson;
import net.ramify.model.record.collection.RecordSet;
import net.ramify.model.record.residence.GenericLifeEventRecord;
import net.ramify.model.record.type.LifeEventRecord;
import net.ramify.model.record.xml.RecordContext;
import net.ramify.model.record.xml.record.XmlPersonRecord;
import net.ramify.model.record.xml.record.XmlRecord;
import net.ramify.utils.objects.Consumers;
import net.ramify.utils.objects.Functions;

import javax.annotation.Nonnull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@XmlRootElement(namespace = XmlRecord.NAMESPACE, name = "residence")
public class XmlResidenceRecord extends XmlPersonRecord {

    @XmlAttribute(name = "placeId", required = false)
    private String placeId;

    @XmlElementRef
    private List<XmlRelationship> relationships;

    @Nonnull
    public LifeEventRecord build(final Place parentPlace, final DateRange date, final RecordContext context, final RecordSet recordSet) {
        final var recordId = this.recordId();
        try {
            final var place = Functions.ifNonNull(placeId, id -> context.places().require(new PlaceId(id)), parentPlace);
            final var person = this.person(place, date, context);
            final var family = this.family(person, date, context);
            return new GenericLifeEventRecord(recordId, recordSet.recordSetId(), family, date);
        } catch (final Exception ex) {
            throw new RuntimeException("Error building residence record for " + recordId, ex);
        }
    }

    Person person(final Place place, final DateRange date, final RecordContext context) {
        final var id = this.personId();
        final var name = this.name(context.nameParser());
        final var gender = this.gender();
        final var events = this.events(id, place, date);
        final var notes = this.notes();
        return new GenericRecordPerson(id, name, gender, events, notes);
    }

    Set<Event> events(final PersonId personId, final Place place, final DateRange date) {
        final var events = Sets.<Event>newHashSet();
        events.add(new GenericResidence(personId, date, place));
        Consumers.ifNonNull(this.birth(personId, date), events::add);
        return events;
    }

    Family family(final Person person, final DateRange date, final RecordContext context) {
        final var builder = new FamilyBuilder();
        builder.addPerson(person);
        if (relationships != null) {
            relationships.forEach(r -> r.addRelationship(person, builder, context.nameParser(), date));
        }
        return builder.build();
    }

}
