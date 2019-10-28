package net.ramify.model.place.region;

import com.google.common.collect.ImmutableSet;
import net.ramify.model.place.Place;
import net.ramify.model.place.PlaceGroupId;
import net.ramify.model.place.PlaceId;
import net.ramify.model.place.proto.PlaceProto;
import net.ramify.model.place.region.manor.Manor;

import javax.annotation.Nonnull;
import java.util.Set;

public class Hundred extends AbstractRegion {

    static final Set<Class<? extends Place>> CHILD_TYPES = ImmutableSet.of(Parish.class, Manor.class);

    private final County parent;

    public Hundred(final PlaceId id, final String name, final County parent, final PlaceGroupId groupId) {
        super(id, name, groupId);
        this.parent = parent;
    }

    @Nonnull
    @Override
    public County parent() {
        return parent;
    }

    @Nonnull
    @Override
    public Set<Class<? extends Place>> childTypes() {
        return CHILD_TYPES;
    }

    @Nonnull
    @Override
    public PlaceProto.PlaceType protoType() {
        return PlaceProto.PlaceType.HUNDRED;
    }
}
