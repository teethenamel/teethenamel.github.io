package net.ramify.model.place;

import com.google.common.collect.Iterables;
import net.ramify.data.proto.BuildsProto;
import net.ramify.model.place.proto.PlaceProto;
import net.ramify.model.util.Link;

import javax.annotation.Nonnull;
import java.util.Set;

public interface PlaceDescription extends HasPlaceId, BuildsProto<PlaceProto.PlaceDescription> {

    @Nonnull
    String description();

    @Nonnull
    Set<Place> alsoSee();

    @Nonnull
    Set<Link> links();

    @Nonnull
    @Override
    default PlaceProto.PlaceDescription toProto() {
        return PlaceProto.PlaceDescription.newBuilder()
                .setDescription(this.description())
                .addAllAlsoSee(Iterables.transform(this.alsoSee(), p -> p.toProto(false)))
                .addAllLink(Iterables.transform(this.links(), Link::toProto))
                .build();
    }

}
