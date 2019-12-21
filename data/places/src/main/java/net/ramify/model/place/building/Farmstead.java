package net.ramify.model.place.building;

import net.ramify.model.place.PlaceGroupId;
import net.ramify.model.place.PlaceId;
import net.ramify.model.place.history.BuildingHistory;
import net.ramify.model.place.proto.PlaceProto;
import net.ramify.model.place.type.Building;
import net.ramify.model.place.type.SettlementOrRegion;

import java.util.Objects;

public class Farmstead extends AbstractBuilding implements Building {

    private static final PlaceProto.PlaceType PLACE_TYPE = placeTypeBuilder("Farmstead").setCanPrefix(false).build();

    private final SettlementOrRegion parent;

    public Farmstead(
            final PlaceId id,
            final String name,
            final SettlementOrRegion parent,
            final PlaceGroupId groupId,
            final BuildingHistory history) {
        super(id, name, groupId, history);
        this.parent = Objects.requireNonNull(parent, "parent");
    }

    @Override
    public SettlementOrRegion parent() {
        return parent;
    }

    @Override
    public PlaceProto.PlaceType protoType() {
        return PLACE_TYPE;
    }

}
