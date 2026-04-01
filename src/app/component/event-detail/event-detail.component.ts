import {CommonModule, NgOptimizedImage} from "@angular/common";
import {Component, OnInit} from '@angular/core';
import {Store} from "@ngrx/store";
import * as moment from "moment";
import {filter} from "rxjs";
import {mapFeature, MapState} from "../../core/map-store";
import {EventDetail} from "../../model/event-detail.model";
import {VideoPlayerComponent} from "./video-player/video-player.component";

@Component({
    selector: "sk-event-detail",
    styleUrls: ["./event-detail.component.scss"],
    templateUrl: "./event-detail.component.html",
    standalone: true,
    imports: [
        CommonModule,
        VideoPlayerComponent,
        NgOptimizedImage
    ],
    providers: []
})
export class EventDetailComponent implements OnInit {
    eventDetail: EventDetail;
    categoryLabel: string;
    categoryValues: { label: string, value: string | null }[] = [
        {
            label: "-",
            value: null
        },
        {
            label: "Crime environnemental",
            value: "CRIME_ENVIRONNEMENTAL"
        },
        {
            label: "Crise climatique",
            value: "CRISE_CLIMATIQUE"
        },
        {
            label: "Délinquance financière",
            value: "DELINQUANCE_FINANCIERE"
        },
        {
            label: "Droits humains",
            value: "DROITS_HUMAINS"
        },
        {
            label: "Santé publique",
            value: "SANTE_PUBLIQUE"
        },
        {
            label: "Secret défense",
            value: "SECRET_DEFENSE"
        },
        {
            label: "Sécurité alimentaire",
            value: "SECURITE_ALIMENTAIRE"
        },
        {
            label: "Violences sexuelles",
            value: "VIOLENCES_SEXUELLES"
        },
        {
            label: "Évènementiel",
            value: "EVENEMENTIEL"
        }
    ];

    constructor(
        private store: Store<MapState>
    ) {
    }

    ngOnInit(): void {
        this.store.select(mapFeature.selectSelectedEventDetail).pipe(
            filter(selectedEvent => !!selectedEvent)
        ).subscribe((eventDetail: EventDetail | null) => {
            this.eventDetail = eventDetail!;
            this.categoryLabel = this.categoryValues.find(c => c.value === eventDetail?.categoryId)?.label || "No category"
        });
    }

    protected getEventInfoText(): string {
        let infoText = "";
        if (this.eventDetail.city) {
            infoText += `À ${this.eventDetail.city}, `;
        }
        infoText += `${this.eventDetail.country} le ${moment(this.eventDetail.eventDate).format("DD/MM/YYYY")}`;
        return infoText;
    }
}
