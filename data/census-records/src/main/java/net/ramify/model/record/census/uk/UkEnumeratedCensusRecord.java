package net.ramify.model.record.census.uk;

import net.ramify.model.event.DateRange;
import net.ramify.model.family.Family;
import net.ramify.model.family.relationship.UnknownRelationship;
import net.ramify.model.person.AgeDetails;
import net.ramify.model.person.Person;
import net.ramify.model.person.PersonalDetails;
import net.ramify.model.person.age.AgeRange;
import net.ramify.model.person.event.PersonalEvents;
import net.ramify.model.person.gender.Gender;
import net.ramify.model.place.address.Address;
import net.ramify.model.record.Record;
import net.ramify.model.record.census.AbstractCensusRecord;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class UkEnumeratedCensusRecord extends AbstractCensusRecord implements UkCensusRecord {

    private final PersonalDetails head;
    private final Set<PersonalDetails> others;

    protected UkEnumeratedCensusRecord(
            final DateRange date,
            final Address address,
            final PersonalDetails head,
            final Map<AgeRange, Integer> maleCount,
            final Map<AgeRange, Integer> femaleCount) {
        super(date, address);
        this.head = head;
        this.others = new HashSet<>();
        this.others.addAll(peopleIn(maleCount, Gender.MALE, head.person().gender() == Gender.MALE));
        this.others.addAll(peopleIn(femaleCount, Gender.FEMALE, head.person().gender() == Gender.FEMALE));
    }

    private static Set<PersonalDetails> peopleIn(final Map<AgeRange, Integer> total, final Gender gender, final boolean removeHead) {
        final Map<AgeRange, Integer> filtered = removeHead ? filterHead(total) : total;
        final Set<PersonalDetails> details = new HashSet<>();
        filtered.forEach((age, count) -> details.addAll(peopleIn(age, count, gender)));
        return details;
    }

    private static Set<PersonalDetails> peopleIn(final AgeRange age, final int count, final Gender gender) {
        final Set<PersonalDetails> all = new HashSet<>(count);
        for (int i = 1; i <= count; i++) {
            final Person person = Person.of(gender);
            final PersonalDetails details = new AgeDetails(person, age);
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
    public Map<Person, PersonalEvents> personalEvents() {
        final DateRange date = this.date();
        final Map<Person, PersonalEvents> events = new HashMap<>();
        events.put(head.person(), head.inferredEvents());
        events.putAll(Record.personalEvents(date, others));
        return events;
    }

    @Nonnull
    @Override
    public Family family() {
        if (others.isEmpty()) {
            return Family.of(head.person());
        }
        return Family.of(UnknownRelationship.between(head.person(), others));
    }

}
