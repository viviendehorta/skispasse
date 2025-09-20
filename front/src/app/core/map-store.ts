import {createActionGroup, createFeature, createReducer, emptyProps, on, props} from "@ngrx/store";
import {LonLat} from "../model/lonlat.model";
import {EventDetail} from "../model/event.model";
import {produce} from "immer";
import * as moment from "moment";

export interface MapState {
    selectingLocation: boolean,
    locationForEventCreation: LonLat | null
    events: EventDetail[]
}

const initialState: MapState = {
    selectingLocation: false,
    locationForEventCreation: null,
    events: []
}

export const mapActions = createActionGroup({
    source: 'Map',
    events: {
        setEvents: props<{events: EventDetail[]}>(),
        loadEvents: emptyProps(),
        toggleSelectingLocation: emptyProps(),
        setLocationForEventCreation: props<{location: LonLat}>(),
        addEvent: props<{title: string, category: string, longitude: number, latitude: number}>(),
    },
});

export const mapFeature = createFeature({
    name: 'Map',
    reducer: createReducer(
        initialState,
        on(mapActions.toggleSelectingLocation, (state, {}) => {
            return {
                ...state,
                selectingLocation: !state.selectingLocation
            };
        }),
        on(mapActions.setLocationForEventCreation, (state, {location}) => {
            return {
                ...state,
                locationForEventCreation: location,
                selectingLocation: false
            };
        }),
        on(mapActions.setEvents, (state, {events}) => {
            return produce(state, draft => {
                draft.events = events
            })
        }),
        on(mapActions.addEvent, (state, {title, category, longitude, latitude}) => {
            return produce(state, draft => {
                draft.events.push({
                    id: "id",
                    title,
                    category,
                    location: {longitude, latitude},
                    media: {
                        contentType: "image/jpeg",
                        url: "/image-url"
                    },
                    created: moment(),
                    eventDate: moment(),
                    address: "adresse du " + title,
                })
            })
        }),
    ),
});
