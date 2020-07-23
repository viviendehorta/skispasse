import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ModalService} from '../../../core/modal/modal.service';
import {LoginModalService} from '../../../core/login/login-modal.service';
import {ROLE_ADMIN, ROLE_CONTRIBUTOR} from '../../../shared/constants/role.constants';
import {AccountService} from '../../../core/auth/account.service';
import {LoginService} from '../../../core/login/login.service';
import {EventManager} from '../../../core/events/event-manager';
import {UserAccount} from "../../../shared/model/account.model";

@Component({
  selector: 'skis-account-panel',
  templateUrl: './account-panel.component.html',
  styleUrls: ['./account-panel.component.scss']
})
export class AccountPanelComponent implements OnInit {

  account: UserAccount | null = null;

  constructor(
    private modalService: ModalService,
    private loginService: LoginService,
    private loginModalService: LoginModalService,
    private accountService: AccountService,
    private eventManager: EventManager,
    private router: Router
  ) {}

  ngOnInit() {
    this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  login() {
    return this.loginModalService.open();
  }

  logout() {
    this.loginService.logout();
    this.router.navigate(['/map']);
  }

  isAuthenticated() {
    return this.accountService.isAuthenticated();
  }

  isAdmin() {
    return this.accountService.hasAnyAuthority(ROLE_ADMIN);
  }

  isContributor() {
    return this.accountService.hasAnyAuthority(ROLE_CONTRIBUTOR);
  }
}
