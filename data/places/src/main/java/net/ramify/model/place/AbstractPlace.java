package net.ramify.model.place;

import net.ramify.model.place.region.Country;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import java.util.Objects;

public abstract class AbstractPlace implements Place {

    private final PlaceId id;
    private final String name;
    private final PlaceGroupId groupId;
    private final PlaceHistory history;

    protected AbstractPlace(final PlaceId id, final String name, final PlaceGroupId groupId, final PlaceHistory history) {
        this.id = Objects.requireNonNull(id, "id");
        this.name = Objects.requireNonNull(name, "name");
        this.groupId = groupId;
        this.history = history;
    }

    @Nonnull
    @Override
    public PlaceId placeId() {
        return id;
    }

    @Nonnull
    @Override
    public String name() {
        return name;
    }

    @CheckForNull
    @Override
    public PlaceGroupId groupId() {
        return groupId;
    }

    @CheckForNull
    @Override
    public PlaceHistory history() {
        return history;
    }

    @Override
    public boolean isDefunct() {
        return (history != null && history.hasClosure()) || DefunctPlaces.isDefunct(this, this.find(Country.class).orElse(null));
    }

}
