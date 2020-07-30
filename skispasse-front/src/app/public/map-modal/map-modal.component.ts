import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {Router} from "@angular/router";

declare var $: any;

@Component({
    selector: 'skis-map-modal',
    templateUrl: './map-modal.component.html',
    styleUrls: ['./map-modal.component.scss']
})
export class MapModalComponent implements OnInit {

    ROUTED_MODAL_ID = 'routedModal';
    mapModal: any;

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

    onModalOutletActivated() {
        this.mapModal.modal('show');
    }
}
