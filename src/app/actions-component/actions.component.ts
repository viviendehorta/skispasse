import {Component, Input} from '@angular/core';
import {Store} from "@ngrx/store";
import exportFromJSON from "export-from-json";
import {Button} from "primeng/button";
import {from} from "rxjs";
import {mapActions, mapFeature, MapState} from "../core/map-store";
import {EventInfo} from "../model/event-info.model";

@Component({
    selector: 'actions',
    standalone: true,
    imports: [
        Button
    ],
    templateUrl: './actions.component.html',
    styleUrl: './actions.component.scss'
})
export class ActionsComponent {
    @Input() isDisableEventCreation: boolean;

    protected isRunningExport: boolean = false;
    protected isRunningImport: boolean = false;

    events: EventInfo[];

    constructor(private mapStore: Store<MapState>) {
        this.mapStore.select(mapFeature.selectEvents).subscribe(events => {
            this.events = events.map(eventDetail => {
                return {
                    title: eventDetail.country,
                    location: eventDetail.location,
                    media: eventDetail.media,
                    categoryId: eventDetail.categoryId,
                    created: eventDetail.created,
                    address: eventDetail.address,
                    eventDate: eventDetail.eventDate,
                    city: eventDetail.city,
                    country: eventDetail.country,
                };
            });
        });
    }

    protected toggleSelectingLocation() {
        this.mapStore.dispatch(mapActions.toggleSelectingLocation());
    }

    protected importEvents() {
        from(fetch("/assets/sample_exports/events_data.json")
            .then(response => {
                if (!response.ok) {
                    this.mapStore.dispatch(mapActions.setInError({errorMessage: `L'import a échoué.`}));
                }
                return response.json();
            }))
            .subscribe((eventsJson: EventInfo[]) => {
                this.mapStore.dispatch(mapActions.importEvents({events: eventsJson}));
            });
    }

    protected exportEvents() {
        this.isRunningExport = true;
        exportFromJSON({
            data: this.events,
            fileName: `skispasse_export_${new Date().toISOString()}`,
            exportType: "json"
        });
        this.isRunningExport = false;
    }
}
