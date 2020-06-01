import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ModalService} from '../../../core/modal/modal.service';
import {LoginModalService} from '../../../core/login/login-modal.service';
import {ROLE_ADMIN, ROLE_CONTRIBUTOR} from '../../../shared/constants/role.constants';
import {AccountService} from '../../../core/auth/account.service';
import {LoginService} from '../../../core/login/login.service';
import {UserAccount} from '../../../shared/model/account.model';
import {EventManager} from '../../../core/events/event-manager';
import {AuthenticationState} from "../../../shared/model/authentication-state.model";

@Component({
  selector: 'skis-account-panel',
  templateUrl: './account-panel.component.html',
  styleUrls: ['./account-panel.component.scss']
})
export class AccountPanelComponent implements OnInit {
  account: UserAccount;

  constructor(
    private modalService: ModalService,
    private loginService: LoginService,
    private loginModalService: LoginModalService,
    private accountService: AccountService,
    private eventManager: EventManager,
    private router: Router
  ) {}

  ngOnInit() {
    this.accountService.getAuthenticationState().subscribe((authenticationState: AuthenticationState) => {
      console.log("AccountPanelComponent : account subscription notification : account=" + JSON.stringify(authenticationState));
      this.account = authenticationState.user;
    });
    console.log("AccountPanelComponent : accountService.getAuthenticationState() DONE !");
  }

  login() {
    return this.loginModalService.open();
  }

  logout() {
    this.loginService.logout();
    this.router.navigate(['']);
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
