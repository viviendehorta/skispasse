import { Injectable } from '@angular/core';
import { ModalService } from 'app/core/modal/modal.service';
import { LoginModalComponent } from 'app/public/login/login.component';

@Injectable({ providedIn: 'root' })
export class LoginModalService {
  constructor(private modalService: ModalService) {}

  open() {
    return this.modalService.open(LoginModalComponent, 'login-modal');
  }
}
