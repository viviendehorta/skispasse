import {Component, OnInit} from '@angular/core'
import {ActivatedRoute} from "@angular/router";
import {EventDetail} from "../../model/event-detail.model";
import {mapFeature, MapState} from "../../core/map-store";
import {Store} from "@ngrx/store";
import {filter} from "rxjs";
import {CommonModule} from "@angular/common";
import {VideoPlayerComponent} from "./video-player/video-player.component";

@Component({
    selector: "sk-event-detail",
    styleUrls: ["./event-detail.component.scss"],
    templateUrl: "./event-detail.component.html",
    standalone: true,
    imports: [
        CommonModule,
        VideoPlayerComponent
    ],
    providers: []
})
export class EventDetailComponent implements OnInit {
    eventDetail: EventDetail

    constructor(
        private route: ActivatedRoute,
        private store: Store<MapState>
    ) {
    }

    ngOnInit(): void {
        this.store.select(mapFeature.selectSelectedEventDetail).pipe(
            filter(selectedEvent => !!selectedEvent)
        ).subscribe(eventDetail => {
            this.eventDetail = eventDetail!
        })
    }

    protected getEventInfoText(): string {
        let infoText = ""
        if (this.eventDetail.city) {
            infoText += `À ${this.eventDetail.city}, `
        }
        infoText += `${this.eventDetail.country} le ${this.eventDetail.eventDate.format("DD/MM/YYYY")}`
        return infoText;
    }
}
