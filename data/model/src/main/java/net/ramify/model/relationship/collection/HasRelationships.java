package net.ramify.model.relationship.collection;

import net.ramify.model.relationship.Relationship;
import net.ramify.utils.collections.IterableUtils;

import javax.annotation.Nonnull;
import java.util.Set;

public interface HasRelationships {

    @Nonnull
    Set<? extends Relationship> relationships();

    default boolean hasRelationship(@Nonnull final Class<? extends Relationship> type) {
        return IterableUtils.any(this.relationships(), r -> r.is(type));
    }

    @Nonnull
    default <R extends Relationship> Set<R> findAllRelationships(@Nonnull final Class<R> type) {
        return IterableUtils.findAll(this.relationships(), type);
    }

}
