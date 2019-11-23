package net.ramify.strategy.match;

import net.ramify.utils.objects.OptionalBoolean;

import javax.annotation.Nonnull;

public interface Matcher<T> {

    @Nonnull
    OptionalBoolean match(T t1, T t2);

}
