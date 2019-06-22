package net.ramify.model.relationship.type;

import com.google.common.collect.Sets;
import net.ramify.model.person.PersonId;

import javax.annotation.Nonnull;
import java.util.Set;

public interface CosanguinealRelationship extends DirectRelationship {

    @Override
    default boolean isDirected() {
        return true;
    }

    @Nonnull
    default Set<PersonId> relatives() {
        return Sets.newHashSet(this.fromId(), this.toId());
    }

    @Override
    default <R> R handleWith(final RelationshipHandler<R> handler) {
        return handler.handle(this);
    }

}
