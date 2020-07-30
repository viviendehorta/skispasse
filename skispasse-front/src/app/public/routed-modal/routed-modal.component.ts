import {Component, OnInit} from '@angular/core';
declare var $: any;

@Component({
    selector: 'skis-routed-modal',
    templateUrl: './routed-modal.component.html'
})
export class RoutedModalComponent implements OnInit {
    ROUTED_MODAL_ID = 'routedModal';

    ngOnInit() {
    }

    open() {
        $('#routedModal').modal('show');
    }
}
