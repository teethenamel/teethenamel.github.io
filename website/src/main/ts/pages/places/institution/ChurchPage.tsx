import * as React from "react";
import {DEFAULT_CHURCH_LOADER} from "../../../components/places/ChurchLoader";
import {PlacesPageProps} from "../PlacesBasePage";
import {AsyncData, asyncLoadData} from "../../../components/fetch/AsyncData";
import {InstitutionInfo} from "./InstitutionInfo";
import {Institution} from "../../../protobuf/generated/institution_pb";
import {PlaceMap} from "../../../components/places/PlaceMap";
import {Loading} from "../../../components/Loading";
import {InstitutionPage} from "./InstitutionPage";
import {PlaceId} from "../../../components/places/Place";

export default class ChurchPage extends InstitutionPage {

    private readonly churchLoader = DEFAULT_CHURCH_LOADER;

    loadInstitution(id: PlaceId) {
        if (!id) return;
        asyncLoadData(id, this.churchLoader.loadChurch, church => this.setState({institution: church}));
    }

}