package net.ramify.utils.collections;

import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class SetUtils {

    public static <F, T> Set<T> transform(final Collection<F> collection, final Function<? super F, ? extends T> transform) {
        if (collection.isEmpty()) return Collections.emptySet();
        final var set = Sets.<T>newHashSetWithExpectedSize(collection.size());
        for (final var element : collection) {
            set.add(transform.apply(element));
        }
        return set;
    }

}
