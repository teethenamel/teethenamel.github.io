package net.ramify.model.place.region;

import net.ramify.model.place.PlaceId;
import net.ramify.model.place.type.Region;

public class Township extends AbstractRegion {

    private final Region parent;

    public Township(final PlaceId id, final String name, final Region parent) {
        super(id, name);
        this.parent = parent;
    }

    @Override
    public Region parent() {
        return parent;
    }

}
