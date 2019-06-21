package net.ramify.model.person;

import net.ramify.model.event.Event;
import net.ramify.model.event.HasEvents;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

public interface HasPeople extends HasEvents {

    @Nonnull
    Set<Person> people();

    default boolean hasPerson(@Nonnull final Person person) {
        return this.people().contains(person);
    }

    @Nonnull
    @Override
    default Set<Event> events() {
        final var events = new HashSet<Event>();
        this.people().forEach(person -> events.addAll(person.events()));
        return events;
    }

}
