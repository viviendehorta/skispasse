import {Injectable} from '@angular/core';
import {delay, Observable, of} from "rxjs";
import {EventDetail} from "../../model/event-detail.model";

@Injectable()
export class EventService {
    events: EventDetail[] = [
        {
            id: "1",
            title: "Manifestation pour la Palestine",
            categoryId: "Droits humains",
            location: {
                latitude: 52.373090992339826,
                longitude: -9.133719679095094
            },
            created: new Date(),
            eventDate: new Date(),
            address: "1 rue Rima Hassan",
            media: {
                type: "IMAGE",
                contentType: "image/jpeg",
                url: "/assets/pictures/city.jpg"
            },
            city: "",
            country: "Irlande"
        }
    ];
    nextIdEvent: number = 2;

    constructor() {
    }

    list(): Observable<EventDetail[]> {
        return of(this.events).pipe(delay(3000));
    }

    addEvent(title: string, category: string, longitude: number, latitude: number): Observable<EventDetail> {
        // return throwError(() => new Error("Erreur back mockée."))
        let eventId = this.nextIdEvent.toString();
        this.nextIdEvent += 1;
        let newEvent: EventDetail = {
            id: eventId,
            title: title,
            city: "",
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
            country: "",
            created: new Date()
        };
        this.events = [...this.events, newEvent];
        return of(newEvent).pipe(delay(3000));
    }
}
