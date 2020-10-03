package net.ramify.model.place.region.district;

import net.ramify.model.place.PlaceGroupId;
import net.ramify.model.place.PlaceId;
import net.ramify.model.place.history.PlaceHistory;
import net.ramify.model.place.proto.PlaceProto;
import net.ramify.model.place.region.AbstractRegion;

import javax.annotation.Nonnull;

public class CivilParish extends AbstractRegion {

    private static final PlaceProto.PlaceType PLACE_TYPE = placeType("Civil Parish");

    private final District parent;

    public CivilParish(
            final PlaceId id,
            final String name,
            final District parent,
            final PlaceGroupId groupId,
            final PlaceHistory history) {
        super(id, name, groupId, history);
        this.parent = parent;
    }

    private District parent() {
        return parent;
    }

    @Nonnull
    @Override
    public PlaceProto.PlaceType protoType() {
        return PLACE_TYPE;
    }

}
