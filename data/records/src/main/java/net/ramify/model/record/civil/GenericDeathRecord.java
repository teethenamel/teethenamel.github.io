package net.ramify.model.record.civil;

import net.ramify.model.date.ExactDate;
import net.ramify.model.event.proto.EventProto;
import net.ramify.model.family.Family;
import net.ramify.model.place.PlaceId;
import net.ramify.model.record.ExactDateRecord;
import net.ramify.model.record.RecordId;
import net.ramify.model.record.collection.RecordSet;
import net.ramify.model.record.type.DeathRecord;

import javax.annotation.Nonnull;

public class GenericDeathRecord extends ExactDateRecord implements DeathRecord {

    private final PlaceId deathPlace;
    private final Family family;

    public GenericDeathRecord(
            final RecordId id,
            final RecordSet recordSet,
            final ExactDate date,
            final PlaceId deathPlace,
            final Family family) {
        super(id, recordSet, date);
        this.deathPlace = deathPlace;
        this.family = family;
    }

    @Nonnull
    @Override
    public Family family() {
        return family;
    }

    @Nonnull
    @Override
    public PlaceId placeId() {
        return deathPlace;
    }

    @Override
    protected EventProto.RecordType protoType() {
        return EventProto.RecordType.DEATH;
    }

}
