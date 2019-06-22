package net.ramify.model.date;

import javax.annotation.Nonnull;
import java.time.chrono.ChronoLocalDate;
import java.util.Objects;
import java.util.Optional;

public class ExactDate implements DateRange {

    private final ChronoLocalDate date;

    public ExactDate(final ChronoLocalDate date) {
        this.date = Objects.requireNonNull(date);
    }

    @Nonnull
    public ChronoLocalDate date() {
        return date;
    }

    @Nonnull
    @Override
    public Optional<? extends ChronoLocalDate> earliestInclusive() {
        return Optional.of(date);
    }

    @Nonnull
    @Override
    public Optional<? extends ChronoLocalDate> latestInclusive() {
        return Optional.of(date);
    }

}
