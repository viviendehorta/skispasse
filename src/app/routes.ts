import {Routes} from '@angular/router';
import {EventCreationComponent} from "./component/event-creation/event-creation.component";
import {EventDetailComponent} from "./component/event-detail/event-detail.component";

export const routes: Routes = [
    {
        path: "event-creation",
        component: EventCreationComponent,
    },
    {
        path: "event-detail/:eventId",
        component: EventDetailComponent,
    },
    {
        path: "**",
        redirectTo: "",
        pathMatch: "full"
    },
];
