import {Component, Input} from '@angular/core';
import {Store} from "@ngrx/store";
import exportFromJSON from "export-from-json";
import {Button} from "primeng/button";
import {from} from "rxjs";
import {mapActions, mapFeature, MapState} from "../core/map-store";
import {EventDetail} from "../model/event-detail.model";

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

    events: EventDetail[];

    constructor(private mapStore: Store<MapState>) {
        this.mapStore.select(mapFeature.selectEvents).subscribe(events => {
            this.events = events;
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
            .subscribe(eventsJson => {
                this.mapStore.dispatch(mapActions.loadEventsSuccess({events: eventsJson}));
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
