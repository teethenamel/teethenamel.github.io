package net.ramify.model.place;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface Place {

    @Nonnull
    String name();

    @Nonnull
    Optional<? extends Position> position();

}
