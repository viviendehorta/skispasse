import {Component, OnInit, ViewChild} from '@angular/core';
import {NavigationEnd, Router, RouterOutlet} from "@angular/router";
import {EventMapComponent} from "./component/events-map/event-map.component";
import {Button} from "primeng/button";
import {Drawer} from "primeng/drawer";
import {mapActions, MapState} from "./core/map-store";
import {Store} from "@ngrx/store";

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
    ],
    providers: []
})
export class AppComponent implements OnInit {
    @ViewChild('contentDrawerRef') drawerRef!: Drawer;
    showContent: boolean = false;

    constructor(
        private router: Router,
        private mapStore: Store<MapState>
    ) {
    }

    ngOnInit(): void {
        this.router.events.subscribe(event => {
            this.showContent = this.needsToogleContent(event);
        })
    }

    private needsToogleContent(event: any) {
        return event instanceof NavigationEnd
            && event.urlAfterRedirects !== ""
            && event.urlAfterRedirects !== "/";
    }

    protected onCloseContentPanel() {
        this.router.navigate([""])
    }

    protected closeCallback(event: MouseEvent) {
        this.drawerRef.close(event);
        this.mapStore.dispatch(mapActions.setSelectedEvent({eventId: null}))
    }

    protected toggleSelectingLocation() {
        this.mapStore.dispatch(mapActions.toggleSelectingLocation())
    }
}
