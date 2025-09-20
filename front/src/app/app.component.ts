import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {Observable} from "rxjs";
import {NewsFact} from "./model/newsfact.model";
import {NewsfactService} from "./core/service/newsfact.service";
import {NewsfactsMapComponent} from "./component/newsfacts-map/newsfacts-map.component";

@Component({
    selector: 'sk-app',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss'],
    standalone: true,
    imports: [
        NewsfactsMapComponent,
        RouterOutlet,
    ]
})
export class AppComponent implements OnInit {
    newsFacts$!: Observable<NewsFact[]>
    selectedNewsFactId: string | null = null

    constructor(private newsfactService: NewsfactService) {
    }

    ngOnInit(): void {
        this.newsFacts$ = this.newsfactService.list()
    }

    closeNewsFactView() {
        this.selectedNewsFactId = null
    }
}
