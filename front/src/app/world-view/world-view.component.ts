import {Component, OnInit} from '@angular/core'
import {NewsFact} from "../model/newsfact.model"
import {NewsfactService} from "../core/service/newsfact.service"
import {Observable} from "rxjs"
import {NewsFactListComponent} from "./newsfact-list/news-fact-list.component";
import {NewsfactsMapComponent} from "./newsfacts-map/newsfacts-map.component";
import {NewsfactViewComponent} from "./newsfact-view/newsfact-view.component";

@Component({
    templateUrl: './world-view.component.html',
    standalone: true,
    imports: [
        NewsFactListComponent,
        NewsfactsMapComponent,
        NewsfactViewComponent,
    ]
})
export class WorldViewComponent implements OnInit {

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
