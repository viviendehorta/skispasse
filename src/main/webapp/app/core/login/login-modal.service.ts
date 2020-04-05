import { Injectable } from '@angular/core';
import { flatMap } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-session.service';
import { LoginService } from 'app/core/login/login.service';
import { ModalService } from 'app/core/modal/modal.service';
import { LoginModalComponent } from 'app/login/login.component';

@Injectable({ providedIn: 'root' })
export class LoginModalService {
  constructor(private modalService: ModalService) {}

  open() {
    return this.modalService.open(LoginModalComponent, 'login-modal');
  }
}
