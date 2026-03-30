import {CommonModule} from "@angular/common";
import {Component, Input, OnInit} from '@angular/core';
import {ButtonModule} from "primeng/button";
import {Observable} from "rxjs";
import {EventDetail} from "../../model/event-detail.model";

@Component({
    templateUrl: "./event-list.component.html",
    selector: "sk-event-list",
    standalone: true,
    imports: [
        ButtonModule,
        CommonModule,
    ]
})
export class EventListComponent implements OnInit {

    @Input() newsFacts$!: Observable<EventDetail[]>;
    newsFacts!: EventDetail[];

    isCollapsed: boolean = false;

    ngOnInit() {
        this.newsFacts$.subscribe(newsFacts => this.newsFacts = newsFacts);
    }

    collapse() {
        this.isCollapsed = true;
    }
}
