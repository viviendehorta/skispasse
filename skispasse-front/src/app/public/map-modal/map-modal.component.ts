import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";

declare var $: any;

@Component({
    selector: 'skis-map-modal',
    templateUrl: './map-modal.component.html'
})
export class MapModalComponent implements OnInit {

    ROUTED_MODAL_ID = 'routedModal';
    mapModal: any;
    routerOutlet: any;

    constructor(
        private router:Router
    ) {}

    ngOnInit() {
        this.mapModal = $('#' + this.ROUTED_MODAL_ID);

        //Go out from the outlet url on closing modal
        this.mapModal.on('hidden.bs.modal', () => {
            this.router.navigate([{ outlets: { modal: null } }]);
        })
    }

    onModalOutletOn() {
        this.mapModal.modal('show');
    }

    /* Mainly used to hide the popup when going back or up in the history to pages that don't use the popup */
    onChange() {
        if (!this.routerOutlet.isActivated) {
            this.mapModal.modal('hide');
        }
    }
}
