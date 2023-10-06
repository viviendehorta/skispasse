import {Component, OnInit} from '@angular/core'
import {NewsFact} from "../model/newsfact.model"

@Component({
    templateUrl: './world-view.component.html',
})
export class WorldViewComponent implements OnInit {

    newsFacts!: NewsFact[]
    selectedNewsFactIds: string[] = []
    isCollapsedSelectedNewsFactsPanel: boolean = true

    constructor() {
    }

    ngOnInit(): void {
    }

    collapseSelectedNewsFactsPanel() {
        this.isCollapsedSelectedNewsFactsPanel = true
    }
}
