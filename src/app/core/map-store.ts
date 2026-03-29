import {createActionGroup, createFeature, createReducer, createSelector, emptyProps, on, props} from "@ngrx/store";
import {LonLat} from "../model/lonlat.model";
import {EventDetail} from "../model/event-detail.model";
import {produce} from "immer";
import {EventService} from "./service/event.service";
import {inject} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {catchError, map, of, switchMap} from "rxjs";

export interface MapState {
    isInError: boolean | null,
    errorMessage: string | null,
    isLoadingEvents: boolean,
    isAddingEvent: boolean,
    selectingLocation: boolean,
    locationForEventCreation: LonLat | null,
    events: EventDetail[],
    selectedEventId: string | null,
    selectedEventDetail: EventDetail | null
}

const initialState: MapState = {
    isInError: false,
    errorMessage: null,
    isLoadingEvents: false,
    isAddingEvent: false,
    selectingLocation: false,
    locationForEventCreation: null,
    events: [],
    selectedEventId: null,
    selectedEventDetail: null
}

export const mapActions = createActionGroup({
    source: 'Map',
    events: {
        loadEvents: emptyProps(),
        loadEventsSuccess: props<{ events: EventDetail[] }>(),
        setInError: props<{ errorMessage: string }>(),
        toggleSelectingLocation: emptyProps(),
        setLocationForEventCreation: props<{ location: LonLat }>(),
        addEvent: props<{ title: string, category: string, longitude: number, latitude: number }>(),
        addEventSuccess: props<{ eventDetail: EventDetail }>(),
        setSelectedEvent: props<{ eventId: string | null }>(),
    },
});

export const mapFeature = createFeature({
    name: 'Map',
    reducer: createReducer(
        initialState,
        on(mapActions.loadEvents, (state, {}) => {
            return produce(state, draft => {
                draft.isLoadingEvents = true
            })
        }),
        on(mapActions.loadEventsSuccess, (state, {events}) => {
            return produce(state, draft => {
                draft.events = events
                draft.isLoadingEvents = false
            })
        }),
        on(mapActions.setInError, (state, {errorMessage}) => {
            return produce(state, draft => {
                draft.isInError = true
                draft.errorMessage = errorMessage
                draft.isLoadingEvents = false
            })
        }),
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
        on(mapActions.addEvent, (state, {}) => {
            return produce(state, draft => {
                draft.isAddingEvent = true
            })
        }),
        on(mapActions.addEventSuccess, (state, {eventDetail}) => {
            return produce(state, draft => {
                draft.events = [...state.events, eventDetail]
                draft.isAddingEvent = false
            })
        }),
        on(mapActions.setSelectedEvent, (state, {eventId}) => {
            return produce(state,  draft => {
                draft.selectedEventId = eventId
            })
        }),
    ),
    extraSelectors: ({ selectEvents, selectSelectedEventId }) => ({
        selectSelectedEventDetail: createSelector(
            selectEvents,
            selectSelectedEventId,
            (events, selectedEventId): EventDetail | null => {
                if (!selectedEventId) {
                    return null
                }
                return events.find(e => e.id === selectedEventId) || null
            }
        ),
    }),
});

const loadEventsEffect = createEffect(
    (actions$ = inject(Actions), eventService = inject(EventService)) => {
        return actions$.pipe(
            ofType(mapActions.loadEvents),
            switchMap(() => eventService.list().pipe(
                map(events => mapActions.loadEventsSuccess({events: events})),
                catchError((error) => {
                    console.error(error)
                    return of(mapActions.setInError({errorMessage: "Erreur de chargement des évènements."}));
                })
            ))
        )
    },
    {functional: true}
)

const addEventEffect = createEffect(
    (actions$ = inject(Actions), eventService = inject(EventService)) => {
        return actions$.pipe(
            ofType(mapActions.addEvent),
            switchMap((action) => eventService.addEvent(
                action.title,
                action.category,
                action.longitude,
                action.latitude,
            ).pipe(
                map(eventDetail => mapActions.addEventSuccess({eventDetail: eventDetail})),
                catchError((error) => {
                    console.error(error)
                    return of(mapActions.setInError({errorMessage: `Erreur de création de l'évènement "${action.title}".`}));
                })
            ))
        )
    },
    {functional: true}
)

export const mapEffects = {
    loadEventsEffect,
    addEventEffect
}
