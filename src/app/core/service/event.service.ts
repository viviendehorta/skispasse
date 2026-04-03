import {Injectable} from '@angular/core';
import {delay, Observable, of} from "rxjs";
import {EventDetail} from "../../model/event-detail.model";
import {EventInfo} from "../../model/event-info.model";

@Injectable()
export class EventService {
    events: EventDetail[] = [
        {
            id: "1",
            title: "Pâques à Florence",
            categoryId: "DROITS_HUMAINS",
            location: {
                latitude: 43.767229828925736,
                longitude: 11.2525000000000
            },
            created: new Date(),
            eventDate: new Date(),
            address: "1 rue Rima Hassan",
            media: {
                type: "IMAGE",
                contentType: "image/jpeg",
                url: "/assets/pictures/duomo_firenze.jpg"
            },
            city: "Florence",
            country: "Italie"
        },
        {
            id: "2",
            title: "Vue d'église londonienne",
            categoryId: "EVENEMENTIEL",
            location: {
                latitude: 51.38801431695356,
                longitude: -0.027829044244547785
            },
            created: new Date(),
            eventDate: new Date(),
            address: "",
            media: {
                type: "IMAGE",
                contentType: "image/jpeg",
                url: "/assets/pictures/eglise.jpg"
            },
            city: "Londres",
            country: "Royaume-Uni"
        },
        {
            id: "3",
            title: "Concert à ciel ouvert à la Havane",
            categoryId: "CRISE_CLIMATIQUE",
            location: {
                latitude: 22.963162461744915,
                longitude: -82.346086657365
            },
            created: new Date(),
            eventDate: new Date(),
            address: "2 rue du Soleil Levant, 93140 Bondy",
            media: {
                type: "IMAGE",
                contentType: "image/jpeg",
                url: "/assets/pictures/la_havane.jpg"
            },
            city: "Dublin",
            country: "Irlande"
        },
        {
            id: "4",
            title: "Sommet dans la brume",
            categoryId: "CRISE_CLIMATIQUE",
            location: {
                latitude: -13.521675330395183,
                longitude: -72.00000000000000
            },
            created: new Date(),
            eventDate: new Date(),
            address: "",
            media: {
                type: "IMAGE",
                contentType: "image/jpeg",
                url: "/assets/pictures/montagne_nuages.jpg"
            },
            city: "Cuzco",
            country: "Pérou"
        },
        {
            id: "5",
            title: "Parapente à Pipa",
            categoryId: "DROITS_HUMAINS",
            location: {
                latitude: -6.242759306124157,
                longitude: -35.47011111111111
            },
            created: new Date(),
            eventDate: new Date(),
            address: "10 rua Lopes Silva, Pipa RN, Brasil",
            media: {
                type: "IMAGE",
                contentType: "image/jpeg",
                url: "/assets/pictures/parapente.jpg"
            },
            city: "Pipa",
            country: "Brésil"
        },
        {
            id: "6",
            title: "Téléphérique au milieu de la favela du Complexo do Alemao de Rio de Janeiro",
            categoryId: "DROITS_HUMAINS",
            location: {
                latitude: -22.843742989252775,
                longitude: -43.441555555555555
            },
            created: new Date(),
            eventDate: new Date(),
            address: "",
            media: {
                type: "IMAGE",
                contentType: "image/jpeg",
                url: "/assets/pictures/telepherique_fleuri.jpg"
            },
            city: "Dublin",
            country: "Irlande"
        },
    ];
    nextIdEvent: number = 7;

    constructor() {
    }

    importEvents(events: EventInfo[]): Observable<EventDetail[]> {
        // return throwError(() => new Error("Erreur import simulée."));
        this.events = [
            ...this.events,
            ...events.map(eventInfo => {
                let newEventId = this.nextIdEvent.toString();
                this.nextIdEvent += 1;
                let newEvent: EventDetail = {
                    id: newEventId,
                    title: eventInfo.title,
                    city: eventInfo.city,
                    address: eventInfo.address,
                    categoryId: eventInfo.categoryId,
                    location: {...eventInfo.location},
                    media: {...eventInfo.media},
                    eventDate: eventInfo.eventDate,
                    country: eventInfo.country,
                    created: eventInfo.created
                };
                return newEvent;
            })
        ]
        return of([...this.events]).pipe(delay(3000));
    }

    listEvents(): Observable<EventDetail[]> {
        return of(this.events);
    }

    addEvent(
        title: string,
        city: string,
        country: string,
        category: string,
        longitude: number,
        latitude: number): Observable<EventDetail> {
        // return throwError(() => new Error("Erreur back mockée."))
        let eventId = this.nextIdEvent.toString();
        this.nextIdEvent += 1;
        let newEvent: EventDetail = {
            id: eventId,
            title: title,
            city: city,
            country: country,
            address: "",
            categoryId: category,
            location: {
                longitude: longitude,
                latitude: latitude
            },
            media: {
                type: "IMAGE",
                contentType: "image/jpeg",
                url: "no-url"
            },
            eventDate: new Date(),
            created: new Date()
        };
        this.events = [...this.events, newEvent];
        return of(newEvent).pipe(delay(3000));
    }
}
