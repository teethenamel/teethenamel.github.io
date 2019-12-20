import * as React from "react";
import {EventBoxProps} from "./EventBox";
import {isExactRange} from "../date/DateRange";
import {EmptyPrefixWords, formatDateRange, formatYearRange} from "../date/DateFormat";
import {MonthDayWordsFormatter} from "../date/DateFormatter";
import {eventType} from "./Event";
import {Button, Checkbox, Icon, Modal} from "antd";
import {Person} from "../../protobuf/generated/person_pb";
import {Event} from "../../protobuf/generated/event_pb";
import {renderAge} from "../people/Age";
import {Loading} from "../style/Loading";
import {DateRange} from "../../protobuf/generated/date_pb";
import {DEFAULT_FAMILY_TREE_LOADER} from "../tree/FamilyTreeLoader";
import {AsyncData, asyncLoadData} from "../fetch/AsyncData";
import {PersonName} from "../people/PersonName";

type Props = EventBoxProps;

type State = {
    modalOpen?: boolean;
    checked: Set<string>;
    inferredBirthDate: AsyncData<DateRange.AsObject>
}

export class EventBoxDate extends React.PureComponent<Props, State> {

    private readonly treeLoader = DEFAULT_FAMILY_TREE_LOADER; //FIXME pass down
    private readonly closeModal = () => this.setState({modalOpen: false});

    constructor(props: Props) {
        super(props);
        this.state = {
            checked: allEventIds(props.person),
            inferredBirthDate: {}
        };
        this.inferBirth = this.inferBirth.bind(this);
    }

    render() {

        const event = this.props.event;
        if (!event) return null;
        const date = event.date;
        const exact = isExactRange(date);

        const person = this.props.person;
        if (!person) return null;

        return <>

            <div className="year">{formatYearRange(date, EmptyPrefixWords)}</div>

            {exact && <div className="date">{formatDateRange(date, MonthDayWordsFormatter, EmptyPrefixWords)}</div>}

            {!exact && eventType(event) == "BIRTH" && <div className="date compute">
                <a onClick={() => this.setState({modalOpen: true})}>Compute</a>
            </div>}

            <Modal
                className="inferBirth"
                title={<>Compute birth date of <PersonName name={person.name}/></>}
                visible={this.state.modalOpen}
                onCancel={this.closeModal}
                footer={<>
                    <Button onClick={this.inferBirth} type="primary" disabled={this.state.inferredBirthDate.loading}>Compute</Button>
                    <Button onClick={this.closeModal}><Icon type="close"/> Close</Button></>}>

                Use the following events:

                {this.props.person.eventsList.map(e => <EventCheckbox
                    key={e.id}
                    event={e}
                    checked={this.state.checked.has(e.id)}
                    disabled={this.state.inferredBirthDate.loading}
                    onCheck={checked => this.toggle(e.id, checked)}/>)}

                {!this.state.inferredBirthDate.loaded && <>Click below to compute.</>}
                {this.state.inferredBirthDate.loading && <Loading/>}
                {this.state.inferredBirthDate.loaded && <>Date: {this.state.inferredBirthDate.data ? formatDateRange(this.state.inferredBirthDate.data, "day") : "Could not compute from given events"}</>}

            </Modal>

        </>;

    }

    componentDidUpdate(prevProps: Readonly<Props>) {
        if (this.props.person && this.props.person != prevProps.person)
            this.setState({modalOpen: false, checked: allEventIds(this.props.person), inferredBirthDate: {}});
    }

    private toggle(id: string, checked: boolean) {
        this.setState(current => {
            const copy = new Set<string>(current.checked);
            if (checked) copy.add(id);
            else copy.delete(id);
            return {checked: copy};
        });
    }

    private inferBirth() {
        asyncLoadData(
            null,
            () => this.treeLoader.loadInferredBirthDate(this.props.tree, this.props.person.id, Array.from(this.state.checked)),
            inferredBirthDate => this.setState({inferredBirthDate}));
    }

}

function allEventIds(person: Person.AsObject): Set<string> {
    if (!person || !person.eventsList) return new Set();
    return new Set(person.eventsList.map(e => e.id));
}

const EventCheckbox = (props: {event: Event.AsObject, checked: boolean, disabled?: boolean, onCheck: (checked: boolean) => void}) => {
    const event = props.event;
    if (!event || !event.givenage) return null;
    return <Checkbox className="date" id={event.id} {...props} onChange={e => props.onCheck(e.target.checked)}>
        <b>{event.title}</b> - age {renderAge(event.givenage)} - {formatDateRange(event.date, "day")}
    </Checkbox>;
};