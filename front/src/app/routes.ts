import {Routes} from '@angular/router';
import {AddEventComponent} from "./component/event-creation/add-event.component";
import {EventDetailComponent} from "./component/event-detail/event-detail.component";

export const routes: Routes = [
    {
        path: "event-creation",
        component: AddEventComponent,
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
