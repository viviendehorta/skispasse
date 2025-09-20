import {Component, OnInit} from '@angular/core'
import {DialogModule} from "primeng/dialog";
import {EventService} from "../../core/service/event.service";
import {ActivatedRoute} from "@angular/router";
import {EventDetail} from "../../model/event.model";

@Component({
    selector: "sk-event-detail",
    templateUrl: "./event-detail.component.html",
    standalone: true,
    imports: [
        DialogModule,
    ],
    providers: [
        EventService,
    ]
})
export class EventDetailComponent implements OnInit {

    eventId: string
    eventDetail: EventDetail

    constructor(
        private eventService: EventService,
        private route: ActivatedRoute,
    ) {
    }

    ngOnInit(): void {
        this.eventId = this.route.snapshot.paramMap.get('eventId') || "";
        this.eventService.getEvent(this.eventId).subscribe(eventDetail => {
            this.eventDetail = eventDetail;
        })
    }
}
