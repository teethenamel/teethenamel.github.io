package net.ramify.model.record.will.probate;

import net.ramify.model.date.DateRange;
import net.ramify.model.event.proto.EventProto;
import net.ramify.model.family.Family;
import net.ramify.model.record.DateFamilyRecord;
import net.ramify.model.record.RecordId;
import net.ramify.model.record.collection.RecordSet;
import net.ramify.model.record.type.PostDeathRecord;

public class ProbateRecord extends DateFamilyRecord implements PostDeathRecord {

    public ProbateRecord(final RecordId id, final RecordSet recordSet, final DateRange date, final Family family) {
        super(id, recordSet, date, family);
    }

    @Override
    protected EventProto.RecordType protoType() {
        return EventProto.RecordType.PROBATE;
    }

}
