package net.ramify.model.person;

import net.ramify.model.event.collection.PersonEventSet;
import net.ramify.model.person.features.PersonFeature;
import net.ramify.model.person.gender.Gender;
import net.ramify.model.person.name.Name;

import javax.annotation.Nonnull;
import java.util.Set;

public class DelegatedPerson implements Person {

    private final Person person;

    public DelegatedPerson(final Person person) {
        this.person = person;
    }

    @Override
    public PersonId personId() {
        return person.personId();
    }

    @Nonnull
    @Override
    public Gender gender() {
        return person.gender();
    }

    @Nonnull
    @Override
    public Name name() {
        return person.name();
    }

    @Override
    public PersonEventSet events() {
        return person.events();
    }

    @Nonnull
    @Override
    public Set<? extends PersonFeature> features() {
        return person.features();
    }

}
