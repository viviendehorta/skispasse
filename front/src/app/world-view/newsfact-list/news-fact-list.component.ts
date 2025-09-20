import {Component, Input, OnInit} from '@angular/core'
import {NewsFact} from "../../model/newsfact.model"
import {Observable} from "rxjs"

@Component({
    templateUrl: "./news-fact-list.component.html",
    selector: "s-newsfact-list"
})
export class NewsFactListComponent implements OnInit {

    @Input() newsFacts$!: Observable<NewsFact[]>
    newsFacts!: NewsFact[]

    isCollapsed: boolean = false

    ngOnInit() {
        this.newsFacts$.subscribe(newsFacts => this.newsFacts = newsFacts)
    }

    collapse() {
        this.isCollapsed = true
    }
}
