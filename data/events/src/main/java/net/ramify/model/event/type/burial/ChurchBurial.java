package net.ramify.model.event.type.burial;

import net.ramify.model.date.DateRange;
import net.ramify.model.event.AbstractEvent;
import net.ramify.model.event.proto.EventProto;
import net.ramify.model.person.PersonId;
import net.ramify.model.person.age.Age;
import net.ramify.utils.objects.Consumers;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.time.Period;

public class ChurchBurial extends AbstractEvent<ChurchBurial> implements Burial {

    private final Age age;

    public ChurchBurial(final PersonId personId, final DateRange date, @CheckForNull final Age age) {
        super(personId, date);
        this.age = age;
    }

    @Nonnull
    @Override
    public EventProto.Event.Builder toProtoBuilder() {
        final var builder = Burial.super.toProtoBuilder();
        Consumers.ifNonNull(age, a -> builder.setGivenAge(a.exact().map(Period::getYears).orElse(0)));
        return builder;
    }
}
