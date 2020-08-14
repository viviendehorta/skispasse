import {Component} from '@angular/core';
import {EventManager} from "../../../core/events/event-manager";

@Component({
    selector: 'skis-contributor-panel',
    templateUrl: './contributor-panel.component.html'
})
export class ContributorPanelComponent {

    constructor(private eventManager: EventManager) {
    }

    switchNewsFactCreationMode() {
        this.eventManager.broadcast({
            name: 'switchNewsFactCreationMode',
            content: null
        });
    }
}
