import { Component, OnInit } from '@angular/core';
import { ROLE_ADMIN, ROLE_CONTRIBUTOR } from 'app/shared/constants/role.constants';
import { ModalService } from 'app/core/modal/modal.service';
import { LoginService } from 'app/core/login/login.service';
import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { JhiEventManager } from 'ng-jhipster';
import { Router } from '@angular/router';
import { Account } from 'app/shared/model/account.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'skis-account-panel',
  templateUrl: './account-panel.component.html',
  styleUrls: ['./account-panel.component.scss']
})
export class AccountPanelComponent implements OnInit {
  account: Account;
  authSubscription: Subscription;

  constructor(
    private modalService: ModalService,
    private loginService: LoginService,
    private loginModalService: LoginModalService,
    private accountService: AccountService,
    private eventManager: JhiEventManager,
    private router: Router
  ) {}

  ngOnInit() {
    this.accountService.identity().subscribe((account: Account) => {
      this.account = account;
    });
    this.registerAuthenticationSuccess();
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
    return this.accountService.getRole() === ROLE_ADMIN;
  }

  isContributor() {
    return this.accountService.getRole() === ROLE_CONTRIBUTOR;
  }

  registerAuthenticationSuccess() {
    this.authSubscription = this.eventManager.subscribe('authenticationSuccess', () => {
      this.accountService.identity().subscribe(account => {
        this.account = account;
      });
    });
  }
}
