import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Injectable({ providedIn: 'root' })
export class ModalService {

  constructor(private ngbModalService: NgbModal) {}

  open(modalComponent: any, modalClass: string): NgbModalRef {

    if (this.ngbModalService.hasOpenModals()) {
      this.ngbModalService.dismissAll('toto');
    }

    const modalRef = this.ngbModalService.open(modalComponent, {
      centered: true,
      size: 'lg',
      windowClass: modalClass
    });
    return modalRef;
  }
}
