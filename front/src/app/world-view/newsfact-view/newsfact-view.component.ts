import {Component, EventEmitter, Input, Output} from '@angular/core'

@Component({
    templateUrl: "./newsfact-view.component.html",
    selector: "s-newsfact-view"
})
export class NewsfactViewComponent {

    @Input() isVisible: boolean = false
    @Input() newsFactId: string | null = null
    @Output() onClose: EventEmitter<any> = new EventEmitter()
}
