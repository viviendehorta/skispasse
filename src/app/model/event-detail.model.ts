import {Moment} from "moment";
import {LonLat} from "./lonlat.model";
import {EventMedia} from "./media.model";

export interface EventDetail {
    id: string
    title: string
    location: LonLat
    media: EventMedia
    category: string
    created: Moment
    address: string
    eventDate: Moment
    city: string,
    country: string
}
