package net.ramify.model.record.provider;

import com.google.common.collect.Sets;
import net.ramify.model.Provider;
import net.ramify.model.record.collection.HasRecordSetId;
import net.ramify.model.record.collection.RecordSet;
import net.ramify.model.record.collection.RecordSetId;
import net.ramify.model.record.collection.RecordSetRelatives;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public interface RecordSetRelativesProvider extends Provider<RecordSetId, RecordSetRelatives> {

    default Optional<RecordSetRelatives> maybeGet(final HasRecordSetId id) {
        return this.maybeGet(id.recordSetId());
    }

    default RecordSet parentOf(final HasRecordSetId id) {
        return this.maybeGet(id).map(RecordSetRelatives::parent).orElse(null);
    }

    default boolean containsAnyParent(final Set<RecordSetId> ids, final HasRecordSetId child) {
        if (child == null) return false;
        if (ids.contains(child.recordSetId())) return true;
        return this.containsAnyParent(ids, this.parentOf(child));
    }

    default Set<RecordSet> allChildren(final HasRecordSetId parentId) {
        final var relatives = this.get(parentId.recordSetId());
        if (relatives == null) return Collections.emptySet();
        final var children = relatives.children();
        if (children.isEmpty()) return Collections.emptySet();
        final var set = Sets.<RecordSet>newHashSet();
        children.forEach(child -> {
            set.add(child);
            set.addAll(this.allChildren(child));
        });
        return set;
    }

    @Nonnull
    default Set<RecordSet> immediateChildren(final HasRecordSetId id) {
        return this.maybeGet(id).map(RecordSetRelatives::children).orElse(Collections.emptySet());
    }

}

