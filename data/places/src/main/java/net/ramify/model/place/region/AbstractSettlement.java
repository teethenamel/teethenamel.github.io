package net.ramify.model.place.region;

import net.ramify.model.place.AbstractPlace;
import net.ramify.model.place.PlaceId;
import net.ramify.model.place.type.Region;
import net.ramify.model.place.type.Settlement;

import javax.annotation.Nonnull;

public abstract class AbstractSettlement extends AbstractPlace implements Settlement {

    private final Region region;

    protected AbstractSettlement(final PlaceId id, final String name, final Region region) {
        super(id, name);
        this.region = region;
    }

    @Nonnull
    @Override
    public Region region() {
        return region;
    }

}
