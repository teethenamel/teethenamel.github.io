package net.ramify.utils.collections;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;
import java.util.function.Function;

public class ListUtils {

    public static <F, T> List<T> eagerlyTransform(final Collection<F> collection, final Function<? super F, ? extends T> transform) {
        if (collection.isEmpty()) return Collections.emptyList();
        if (collection instanceof List && collection instanceof RandomAccess) return eagerlyTransform((List<F>) collection, transform);
        final var list = new ArrayList<T>(collection.size());
        for (final var element : collection) {
            list.add(transform.apply(element));
        }
        return list;
    }

    public static <F, T> List<T> eagerlyTransform(final List<F> list, final Function<? super F, ? extends T> transform) {
        if (!(list instanceof RandomAccess)) return eagerlyTransform((Collection<F>) list, transform);
        final var out = new ArrayList<T>(list.size());
        for (int i = 0; i < list.size(); i++) {
            out.add(transform.apply(list.get(i)));
        }
        return out;
    }

    public static <T> List<T> prefix(final T element, final Collection<? extends T> rest) {
        if (rest.isEmpty()) return Collections.singletonList(element);
        final var list = Lists.<T>newArrayListWithExpectedSize(1 + rest.size());
        list.add(element);
        list.addAll(rest);
        return list;
    }

    public static <T> List<T> reversed(final List<T> list) {
        if (list.size() <= 1) return list;
        return reversedCopy(list);
    }

    public static <T> List<T> reversedCopy(final List<? extends T> list) {
        final List<T> out = Lists.newArrayList(list);
        Collections.reverse(out);
        return out;
    }

}
