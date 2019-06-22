package net.ramify.model.person;

import net.ramify.model.Id;

import java.util.UUID;

public class PersonId extends Id implements HasPersonId {

    public PersonId(final String value) {
        super(value);
    }

    @Override
    public PersonId personId() {
        return this;
    }

    @Override
    public boolean isUnknown() {
        return super.isUnknown() || this.value().startsWith("unknown:");
    }

    public static PersonId unknown() {
        return new PersonId("unknown:" + UUID.randomUUID());
    }

}
