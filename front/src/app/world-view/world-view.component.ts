import {Component, OnInit} from '@angular/core'
import {NewsFact} from "../model/newsfact.model"
import {NewsfactService} from "../core/service/newsfact.service"

@Component({
    templateUrl: './world-view.component.html',
})
export class WorldViewComponent implements OnInit {

    newsFacts!: NewsFact[]
    selectedNewsFactIds: string[] = []
    isCollapsedSelectedNewsFactsPanel: boolean = true

    constructor(private newsfactService: NewsfactService) {
    }

    ngOnInit(): void {
        this.newsfactService.list().subscribe(newsFacts => {
            this.newsFacts = newsFacts
        })
    }

    collapseSelectedNewsFactsPanel() {
        this.isCollapsedSelectedNewsFactsPanel = true
    }
}
