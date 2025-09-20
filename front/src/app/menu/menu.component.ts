import {Component} from '@angular/core'
import {MenuLogoComponent} from "./menu-logo/menu-logo.component";

@Component({
    selector: "s-menu",
    standalone: true,
    templateUrl: "./menu.component.html",
    imports: [
        MenuLogoComponent
    ]
})
export class MenuComponent {

    constructor() {
    }
}
