package net.ramify.model.record.uk.census;

import net.ramify.model.date.DateRange;
import net.ramify.model.event.Histories;
import net.ramify.model.event.PersonalEvents;
import net.ramify.model.family.Family;
import net.ramify.model.family.relationship.UnknownRelationship;
import net.ramify.model.person.NameAgeGender;
import net.ramify.model.person.PersonId;
import net.ramify.model.person.PersonalAttributes;
import net.ramify.model.person.age.AgeRange;
import net.ramify.model.person.gender.Gender;
import net.ramify.model.person.name.Name;
import net.ramify.model.place.address.Address;
import net.ramify.model.record.residence.AbstractCensusRecord;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class UkEnumeratedCensusRecord
        extends AbstractCensusRecord
        implements UkCensusRecord {

    private final PersonalAttributes head;
    private final Set<PersonalAttributes> others;

    protected UkEnumeratedCensusRecord(
            final DateRange date,
            final Address address,
            final PersonalAttributes head,
            final Map<AgeRange, Integer> maleCount,
            final Map<AgeRange, Integer> femaleCount) {
        super(date, address);
        this.head = head;
        this.others = new HashSet<>();
        this.others.addAll(peopleIn(maleCount, Gender.MALE, head.gender() == Gender.MALE, date));
        this.others.addAll(peopleIn(femaleCount, Gender.FEMALE, head.gender() == Gender.FEMALE, date));
    }

    private static Set<PersonalAttributes> peopleIn(final Map<AgeRange, Integer> total, final Gender gender, final boolean removeHead, final DateRange date) {
        final Map<AgeRange, Integer> filtered = removeHead ? filterHead(total) : total;
        if (filtered.isEmpty()) {
            return Set.of();
        }
        final Set<PersonalAttributes> details = new HashSet<>();
        filtered.forEach((age, count) -> details.addAll(peopleIn(age, count, gender, date)));
        return details;
    }

    private static Set<PersonalAttributes> peopleIn(final AgeRange age, final int count, final Gender gender, final DateRange date) {
        final Set<PersonalAttributes> all = new HashSet<>(count);
        for (int i = 1; i <= count; i++) {
            final PersonId person = new CensusPersonId();
            final PersonalAttributes details = new NameAgeGender(person, Name.UNKNOWN, gender, age, date);
            all.add(details);
        }
        return all;
    }

    private static Map<AgeRange, Integer> filterHead(final Map<AgeRange, Integer> total) {
        if (total.size() == 1 && total.values().iterator().next() == 1) {
            return Map.of();
        }
        //TODO check age ranges
        return total;
    }

    @Nonnull
    @Override
    public Histories histories() {
        final Map<PersonId, PersonalEvents> events = new HashMap<>();
        events.put(head.personId(), head.inferredEvents());
        others.forEach(a -> events.put(a.personId(), a.inferredEvents()));
        return Histories.of(events);
    }

    @Nonnull
    @Override
    public Family family() {
        if (others.isEmpty()) {
            return Family.of(head.personId());
        }
        return Family.of(UnknownRelationship.between(head.personId(), others));
    }

    private static class CensusPersonId implements PersonId {

    }

}
