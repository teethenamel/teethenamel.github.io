package net.ramify.model.record.census.uk;

import net.ramify.model.event.DateRange;
import net.ramify.model.family.Family;
import net.ramify.model.family.relationship.Relationship;
import net.ramify.model.person.Person;
import net.ramify.model.person.PersonalDetails;
import net.ramify.model.person.event.PersonalEvents;
import net.ramify.model.person.event.Residence;
import net.ramify.model.place.address.Address;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class UkNamedCensusRecord<T extends PersonalDetails> implements UkCensusRecord {

    private final DateRange date;
    private final Address address;
    private final T head;
    private final Collection<T> others;

    public UkNamedCensusRecord(
            final DateRange date,
            final Address address,
            final T head,
            final Collection<T> others) {
        this.date = date;
        this.address = address;
        this.head = head;
        this.others = others;
    }

    @Nonnull
    @Override
    public Address address() {
        return address;
    }

    @Nonnull
    @Override
    public DateRange date() {
        return date;
    }

    @Nonnull
    @Override
    public Map<Person, PersonalEvents> personalEvents() {
        final Residence residence = this.residence();
        final Map<Person, PersonalEvents> events = new HashMap<>();
        events.put(head.person(), PersonalEvents.of(residence, head.inferredEvents(date)));
        throw new UnsupportedOperationException(); //TODO
    }

    @Nonnull
    @Override
    public Family family() {
        if (others.isEmpty()) {
            return Family.of(head.person());
        }
        final Set<Relationship> relationships = new HashSet<>(others.size());
        others.forEach(person -> relationships.add(this.relationshipBetween(head, person)));
        return Family.of(relationships);
    }

    protected abstract Relationship relationshipBetween(T head, T other);

}
