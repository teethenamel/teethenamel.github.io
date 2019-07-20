package net.ramify.model.record.church;

import net.ramify.model.date.ExactDate;
import net.ramify.model.place.HasPlaceId;
import net.ramify.model.place.PlaceId;
import net.ramify.model.record.ExactDateRecord;
import net.ramify.model.record.RecordId;
import net.ramify.model.record.collection.RecordSetId;

public abstract class AbstractChurchRecord extends ExactDateRecord implements HasPlaceId {

    private final PlaceId church;

    protected AbstractChurchRecord(
            final RecordId id,
            final RecordSetId recordSetId,
            final ExactDate date,
            final PlaceId church) {
        super(id, recordSetId, date);
        this.church = church;
    }

    @Override
    public PlaceId placeId() {
        return church;
    }

}
