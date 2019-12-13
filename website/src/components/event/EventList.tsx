import * as React from "react";
import {EventBox} from "./EventBox";
import {Person} from "../../protobuf/generated/person_pb";
import {Family} from "../../protobuf/generated/family_pb";
import {FamilyTreeId} from "../tree/FamilyTree";
import "./EventList.css";
import {Relatives} from "../relationship/Relatives";
import {ProfileEvent} from "./ProfileEvent";

type Props = {
    events: ReadonlyArray<ProfileEvent>;
    person: Person.AsObject;
    family: Family.AsObject;
    tree: FamilyTreeId;
    relatives: Relatives
}

export class EventList extends React.PureComponent<Props> {

    render() {

        const events = this.props.events || [];

        //FIXME add event ID as key
        return <>
            {events.map(e => <EventBox
                {...this.props}
                {...e}
                relatives={e.type == "person" && this.props.relatives}/>)}
        </>;

    }

}