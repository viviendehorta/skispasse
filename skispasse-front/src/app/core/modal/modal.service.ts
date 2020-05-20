import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Injectable({ providedIn: 'root' })
export class ModalService {
  private isOpen = false;

  constructor(private ngbModalService: NgbModal) {}

  open(modalComponent: any, modalClass: string): NgbModalRef {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;
    const modalRef = this.ngbModalService.open(modalComponent, {
      centered: true,
      windowClass: modalClass
    });
    modalRef.result.finally(() => (this.isOpen = false));
    return modalRef;
  }
}
