import {Component, OnInit} from '@angular/core';
import {NavigationStart, Router} from "@angular/router";

declare var $: any;

@Component({
    selector: 'skis-map-modal',
    templateUrl: './map-modal.component.html'
})
export class MapModalComponent implements OnInit {

    ROUTED_MODAL_ID = 'routedModal';
    mapModal: any;

    constructor(
        private router: Router
    ) {
    }

    ngOnInit() {
        this.mapModal = $('#' + this.ROUTED_MODAL_ID);

        //Leave the outlet url on modal hiding
        this.mapModal.on('hidden.bs.modal', () => {
            this.router.navigate([{outlets: {modal: null}}]);
        });

        this.router.events.subscribe((event) => {
            if (event instanceof NavigationStart) {
                if (event.url.includes('(modal:')) {
                    this.mapModal.modal('show');
                } else {
                    this.mapModal.modal('hide');
                }
            }
        });
    }
}
