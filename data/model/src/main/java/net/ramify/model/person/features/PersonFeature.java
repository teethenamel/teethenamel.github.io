package net.ramify.model.person.features;

import net.meerkat.objects.Castable;
import net.ramify.data.proto.BuildsProto;
import net.ramify.model.person.proto.PersonProto;

import javax.annotation.Nonnull;

public interface PersonFeature extends Castable<PersonFeature>, BuildsProto<PersonProto.PersonFeature> {

    @Nonnull
    String name();

    @Nonnull
    String value();

    @Nonnull
    default PersonProto.PersonFeature.Builder toProtoBuilder() {
        return PersonProto.PersonFeature.newBuilder()
                .setName(this.name())
                .setValue(this.value());
    }

    @Nonnull
    @Override
    default PersonProto.PersonFeature toProto() {
        return this.toProtoBuilder().build();
    }

}
