import {Injectable} from '@angular/core';
import {EventDetail} from "../../model/event.model"
import {HttpClient} from "@angular/common/http"
import {EVENT_URL} from "../../constants"
import {JSON_REQUEST_HEADER_OPTIONS} from "../util/http-utils"
import {Observable, of} from "rxjs"
import * as moment from 'moment'

@Injectable()
export class EventService {
    events: EventDetail[] = [
        {
            id: "1",
            title: "Manifestation pour la Palestine",
            category: "1",
            location: {
                latitude: 52.373090992339826,
                longitude: -9.133719679095094
                // latitude: 0,
                // longitude: 0
            },
            created: moment(),
            eventDate: moment(),
            address: "1 rue Rima Hassan",
            media: {
                contentType: "image/jpeg",
                url: "/image-url"
            }
        }
    ];

    constructor(private http: HttpClient) {
    }

    list(): Observable<EventDetail[]> {
        // return this.http.get<EventDetail[]>(`${EVENT_URL}/list`, JSON_REQUEST_HEADER_OPTIONS)
        return of(this.events)
    }

    getEvent(eventId: string): Observable<EventDetail> {
        return this.http.get<EventDetail>(`${EVENT_URL}/detail/${eventId}`, JSON_REQUEST_HEADER_OPTIONS)
    }
}
