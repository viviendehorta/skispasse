import {LonLat} from "./lonlat.model";
import {EventMedia} from "./media.model";

export interface EventDetail {
    id: string
    title: string
    location: LonLat
    media: EventMedia
    categoryId: string
    created: Date
    address: string
    eventDate: Date
    city: string,
    country: string
}
