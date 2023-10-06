import {Component} from "@angular/core"
import {Router} from "@angular/router"

@Component({
    selector: "v-menu-logo",
    templateUrl: "./menu-logo.component.html"
})
export class MenuLogoComponent {


    constructor(private router: Router) {
    }

    goHome() {
        this.router.navigate(["/"])
    }
}
