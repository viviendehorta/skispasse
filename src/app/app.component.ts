import {Component, ViewChild} from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from "@angular/router";
import {Store} from "@ngrx/store";
import {Button} from "primeng/button";
import {Drawer} from "primeng/drawer";
import {Toast} from "primeng/toast";
import {filter} from "rxjs";
import {ActionsComponent} from "./actions-component/actions.component";
import {EventMapComponent} from "./component/events-map/event-map.component";
import {mapActions, mapFeature, MapState} from "./core/map-store";
import {ToastMessageService} from "./core/service/toast-message.service";

@Component({
    selector: 'sk-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    standalone: true,
    imports: [
        EventMapComponent,
        RouterOutlet,
        Drawer,
        Button,
        Toast,
        ActionsComponent,
    ],
    providers: [
        ToastMessageService
    ]
})
export class AppComponent {
    @ViewChild('contentDrawerRef') drawerRef!: Drawer;
    isActiveContentModal: boolean = false;

    constructor(
        private router: Router,
        private mapStore: Store<MapState>,
        private readonly toastMessageService: ToastMessageService
    ) {
        this.mapStore.select(mapFeature.selectErrorMessage)
            .pipe(filter(errorMessage => !!errorMessage))
            .subscribe(errorMessage => {
                this.toastMessageService.displayError(errorMessage!);
            });
        this.mapStore.select(mapFeature.selectSelectedEventId)
            .pipe(filter(eventId => eventId === null))
            .subscribe(() => {
                this.isActiveContentModal = false;
                this.goBackHome();
            });
        this.router.events.subscribe(event => {
            this.isActiveContentModal = event instanceof NavigationEnd
                && event.urlAfterRedirects !== ""
                && event.urlAfterRedirects !== "/";
        });
    }

    protected onCloseContentPanel() {
        this.goBackHome();
    }

    private goBackHome() {
        this.router.navigate([""]);
    }

    protected closeCallback(event: MouseEvent) {
        this.drawerRef.close(event);
        this.mapStore.dispatch(mapActions.setSelectedEvent({eventId: null}));
    }
}
