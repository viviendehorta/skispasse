import {Component} from '@angular/core';
import {EventManager} from "../../../core/events/event-manager";

@Component({
    selector: 'skis-contributor-panel',
    templateUrl: './contributor-panel.component.html'
})
export class ContributorPanelComponent {

    constructor(private eventManager: EventManager) {
    }

    switchToNewsFactCreationMode() {
        this.eventManager.broadcast({
            name: 'newsFactCreationModeOn',
            content: null
        });
    }
}
