import {Component} from "@angular/core"
import {Router} from "@angular/router"

@Component({
    selector: "s-menu-logo",
    templateUrl: "./menu-logo.component.html",
    standalone: true
})
export class MenuLogoComponent {


    constructor(private router: Router) {
    }

    goHome() {
        this.router.navigate(["/"])
    }
}
