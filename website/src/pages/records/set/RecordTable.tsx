import * as React from "react";
import {Record, RecordSet} from "../../../protobuf/generated/record_pb";
import {Button, Icon, Table} from "antd";
import {RecordPaginationHandler} from "../../../components/records/RecordPaginationHandler";
import {ColumnProps} from "antd/es/table";
import {Person} from "../../../protobuf/generated/person_pb";
import {nameToString} from "../../../components/people/Name";
import {Event} from "../../../protobuf/generated/event_pb";
import {isBirthEvent, isBurialEvent, isDeathEvent} from "../../../components/event/Event";
import {FormattedDateRange, FormattedYearRange} from "../../../components/date/FormattedDateRange";

type Props = Partial<RecordPaginationHandler> & {
    recordSet: RecordSet.AsObject;
    loading: boolean;
    records: ReadonlyArray<Record.AsObject>
}

type State = {
    data: IndividualRecord[];
    columns: RecordColumn[]
}

type IndividualRecord = {
    person: Person.AsObject;
    birth?: Event.AsObject;
    death?: Event.AsObject;
    image?: string;
}

export class RecordTable extends React.PureComponent<Props, State> {

    constructor(props) {
        super(props);
        const properties = {};
        const data = buildIndividualRecords(props.records, properties);
        const columns = determineColumns(properties);
        this.state = {data, columns};
    }


    render() {

        const data = this.state.data;
        if (!data || !data.length) return <div className="noData">No records found.</div>;

        return <Table
            loading={this.props.loading}
            dataSource={data}
            columns={this.state.columns}/>;

    }

    componentDidUpdate(prevProps: Readonly<Props>) {
        if (this.props.records != prevProps.records) {
            const properties = {};
            const data = buildIndividualRecords(this.props.records, properties);
            const columns = determineColumns(properties);
            this.setState({data, columns});
        }
    }

}

type RecordProperties = {
    hasBirth?: boolean;
    hasDeath?: boolean;
}

function buildIndividualRecords(records: ReadonlyArray<Record.AsObject>, properties: RecordProperties): IndividualRecord[] {
    if (!records || !records.length) return [];
    const out: IndividualRecord[] = [];
    records.forEach(record => {
        record.familyList.forEach(family => {
            family.personList.forEach(person => out.push(createRecord(person)));
        });
    });
    properties.hasBirth = out.some(r => r.birth);
    properties.hasDeath = out.some(r => r.death);
    return out;
}

function createRecord(person: Person.AsObject): IndividualRecord {
    const record: IndividualRecord = {person};
    record.birth = person.eventsList.find(isBirthEvent);
    record.death = person.eventsList.find(isDeathEvent) || person.eventsList.find(isBurialEvent);
    return record;
}

function determineColumns(properties: RecordProperties): RecordColumn[] {
    const columns = [ImageColumn, NameColumn];
    if (properties.hasBirth) columns.push(BirthYearColumn);
    if (properties.hasDeath) columns.push(DeathDateColumn);
    columns.push(NotesColumn);
    return columns;
}

type RecordColumn = ColumnProps<IndividualRecord>;

const ImageColumn: RecordColumn = {
    key: "image",
    dataIndex: "image",
    render: t => <Button disabled={!t} title={t ? "View source image" : "No source image available"}><Icon type="file-image"/></Button>,
    width: 30,
};

const NameColumn: RecordColumn = {
    key: "name",
    title: "Name",
    dataIndex: "person.name.surname",
    render: (t, r) => nameToString(r.person.name),
    width: 200,
};

const NotesColumn: RecordColumn = {
    key: "notes",
    title: "Notes",
    dataIndex: "person.notes"
};

const BirthYearColumn: RecordColumn = {
    key: "birthDate",
    title: "Birth date",
    dataIndex: "birth.date",
    render: (t, r) => r.birth && <FormattedYearRange date={r.birth.date}/>,
    width: 120
};

const DeathDateColumn: RecordColumn = {
    key: "deathDate",
    title: "Death date",
    dataIndex: "death.date",
    render: (t, r) => r.death && <FormattedDateRange date={r.death.date} accuracy="day"/>,
    width: 120
};