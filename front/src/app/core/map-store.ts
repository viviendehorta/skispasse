import {createActionGroup, createFeature, createReducer, emptyProps, on, props} from "@ngrx/store";
import {LonLat} from "../model/lonlat.model";
import {EventDetail} from "../model/event-detail.model";
import {produce} from "immer";
import * as moment from "moment";
import {EventService} from "./service/event.service";
import {inject} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {map, mergeMap, of} from "rxjs";

export interface MapState {
    selectingLocation: boolean,
    locationForEventCreation: LonLat | null
    events: EventDetail[],
    selectedEventId: string | null
    selectedEventDetail: EventDetail | null
}

const initialState: MapState = {
    selectingLocation: false,
    locationForEventCreation: null,
    events: [],
    selectedEventId: null,
    selectedEventDetail: null
}

export const mapActions = createActionGroup({
    source: 'Map',
    events: {
        setEvents: props<{ events: EventDetail[] }>(),
        loadEvents: emptyProps(),
        toggleSelectingLocation: emptyProps(),
        setLocationForEventCreation: props<{ location: LonLat }>(),
        addEvent: props<{ title: string, category: string, longitude: number, latitude: number }>(),
        setSelectedEvent: props<{ eventId: string | null }>(),
        setSelectedEventDetail: props<{ eventDetail: EventDetail | null }>(),
    },
});

let currentEventId: number = 2;

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
                let eventId = currentEventId.toString();
                currentEventId += 1;
                draft.events.push({
                    id: eventId,
                    title,
                    category,
                    location: {longitude, latitude},
                    media: {
                        type: "IMAGE",
                        contentType: "image/jpeg",
                        url: "/assets/pictures/city.jpg"
                    },
                    created: moment(),
                    eventDate: moment(),
                    address: "adresse du " + title,
                    city: null,
                    country: "Irlande"
                })
            })
        }),
        on(mapActions.setSelectedEvent, (state, {eventId}) => {
            return produce(state, draft => {
                draft.selectedEventId = eventId
            })
        }),
        on(mapActions.setSelectedEventDetail, (state, {eventDetail}) => {
            return produce(state, draft => {
                draft.selectedEventDetail = eventDetail
            })
        }),
    ),
});

const loadEventDetailEffect = createEffect(
    (actions$ = inject(Actions), eventService = inject(EventService)) => {
        return actions$.pipe(
            ofType(mapActions.setSelectedEvent),
            mergeMap(setSelectedEventAction => {
                if (!setSelectedEventAction.eventId) {
                    return of(mapActions.setSelectedEventDetail({eventDetail: null}))
                }
                return eventService.getEvent(setSelectedEventAction.eventId).pipe(
                    map((eventDetail) => mapActions.setSelectedEventDetail({eventDetail: eventDetail})),
                )
            })
        );
    },
    {functional: true}
);

export const mapEffects = {
    loadEventDetailEffect
}
