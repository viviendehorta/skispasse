import {Component, EventEmitter, Input, Output} from '@angular/core'
import {DialogModule} from "primeng/dialog";

@Component({
    templateUrl: "./newsfact-view.component.html",
    selector: "sk-newsfact-view",
    standalone: true,
    imports: [
        DialogModule,
    ]
})
export class NewsfactViewComponent {

    @Input() isVisible: boolean = false
    @Input() newsFactId: string | null = null
    @Output() onClose: EventEmitter<any> = new EventEmitter()
}
