import {LonLat} from "./lonlat.model"
import {Moment} from "moment"
import {Media} from "./media.model"

export interface NewsFact {

    id: string
    title: string
    location: LonLat
    media: Media
    category: string
    created: Moment
    address: string
    eventDate: Moment
}
