import {Component, OnInit} from '@angular/core'
import {NewsFact} from "../model/newsfact.model"
import {NewsfactService} from "../core/service/newsfact.service"
import {Observable} from "rxjs"

@Component({
    templateUrl: './world-view.component.html',
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
