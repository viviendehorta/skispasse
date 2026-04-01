import {inject} from "@angular/core";
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {createActionGroup, createFeature, createReducer, createSelector, emptyProps, on, props} from "@ngrx/store";
import {produce} from "immer";
import {catchError, map, of, switchMap} from "rxjs";
import {EventDetail} from "../model/event-detail.model";
import {EventInfo} from "../model/event-info.model";
import {LonLat} from "../model/lonlat.model";
import {EventService} from "./service/event.service";

export interface MapState {
    errorMessage: string | null,
    events: EventDetail[],
    isAddingEvent: boolean,
    isImportingEvents: boolean,
    isInError: boolean,
    isLoadingEvents: boolean,
    locationForEventCreation: LonLat | null,
    selectedEventId: string | null,
    selectingLocation: boolean,
}

const initialState: MapState = {
    errorMessage: null,
    events: [],
    isAddingEvent: false,
    isImportingEvents: false,
    isInError: false,
    isLoadingEvents: false,
    locationForEventCreation: null,
    selectedEventId: null,
    selectingLocation: false,
};

export const mapActions = createActionGroup({
    source: 'Map',
    events: {
        addEvent: props<{ title: string, category: string, longitude: number, latitude: number }>(),
        addEventSuccess: props<{ eventDetail: EventDetail }>(),

        importEvents: props<{ events: EventInfo[] }>(),
        importEventsSuccess: props<{ allEvents: EventDetail[] }>(),

        loadEvents: emptyProps(),
        loadEventsSuccess: props<{ events: EventDetail[] }>(),

        setInError: props<{ errorMessage: string }>(),
        setLocationForEventCreation: props<{ location: LonLat }>(),
        setSelectedEvent: props<{ eventId: string | null }>(),
        toggleSelectingLocation: emptyProps(),
    },
});

export const mapFeature = createFeature({
    name: 'Map',
    reducer: createReducer(
        initialState,
        on(mapActions.addEvent, (state, {}) => {
            return produce(state, draft => {
                draft.isAddingEvent = true;
            });
        }),
        on(mapActions.addEventSuccess, (state, {eventDetail}) => {
            return produce(state, draft => {
                draft.events = [...state.events, eventDetail];
                draft.locationForEventCreation = null;
                draft.isAddingEvent = false;
            });
        }),
        on(mapActions.importEvents, (state, {events}) => {
            return produce(state, draft => {
                draft.isImportingEvents = true;
            });
        }),
        on(mapActions.importEventsSuccess, (state, {allEvents}) => {
            return produce(state, draft => {
                draft.events = allEvents;
                draft.selectedEventId = null;
                draft.isImportingEvents = false;
            });
        }),
        on(mapActions.loadEvents, (state, {}) => {
            return produce(state, draft => {
                draft.isLoadingEvents = true;
            });
        }),
        on(mapActions.loadEventsSuccess, (state, {events}) => {
            return produce(state, draft => {
                draft.events = events;
                draft.isLoadingEvents = false;
            });
        }),
        on(mapActions.setInError, (state, {errorMessage}) => {
            return produce(state, draft => {
                draft.isInError = true;
                draft.errorMessage = errorMessage;
                draft.isLoadingEvents = false;
                draft.isAddingEvent = false;
            });
        }),
        on(mapActions.setLocationForEventCreation, (state, {location}) => {
            return {
                ...state,
                locationForEventCreation: location,
                selectingLocation: false
            };
        }),
        on(mapActions.setSelectedEvent, (state, {eventId}) => {
            return produce(state, draft => {
                draft.selectedEventId = eventId;
            });
        }),
        on(mapActions.toggleSelectingLocation, (state, {}) => {
            return {
                ...state,
                selectingLocation: !state.selectingLocation
            };
        }),
    ),
    extraSelectors: ({selectEvents, selectSelectedEventId}) => ({
        selectSelectedEventDetail: createSelector(
            selectEvents,
            selectSelectedEventId,
            (events, selectedEventId): EventDetail | null => {
                if (!selectedEventId) {
                    return null;
                }
                return events.find(e => e.id === selectedEventId) || null;
            }
        ),
    }),
});

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
                    console.error(error);
                    return of(mapActions.setInError({errorMessage: `Erreur de création de l'évènement "${action.title}".`}));
                })
            ))
        );
    },
    {functional: true}
);

const importEventsEffect = createEffect(
    (actions$ = inject(Actions), eventService = inject(EventService)) => {
        return actions$.pipe(
            ofType(mapActions.importEvents),
            switchMap(action => eventService.importEvents(action.events).pipe(
                map(allEvents => mapActions.importEventsSuccess({allEvents: allEvents})),
                catchError((error) => {
                    console.error(error);
                    return of(mapActions.setInError({errorMessage: error}));
                })
            ))
        );
    },
    {functional: true}
);

const loadEventsEffect = createEffect(
    (actions$ = inject(Actions), eventService = inject(EventService)) => {
        return actions$.pipe(
            ofType(mapActions.loadEvents),
            switchMap(() => eventService.listEvents().pipe(
                map(events => mapActions.loadEventsSuccess({events: events})),
                catchError((error) => {
                    console.error(error);
                    return of(mapActions.setInError({errorMessage: "Erreur de chargement des évènements."}));
                })
            ))
        );
    },
    {functional: true}
);

export const mapEffects = {
    addEventEffect,
    importEventsEffect,
    loadEventsEffect
};
