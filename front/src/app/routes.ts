import {Routes} from '@angular/router';
import {EventCreationComponent} from "./component/event-creation/event-creation.component";

export const routes: Routes = [
    {
        path: "add-event",
        component: EventCreationComponent,
    },
    {
        path: "**",
        redirectTo: "",
        pathMatch: "full"
    },
];
