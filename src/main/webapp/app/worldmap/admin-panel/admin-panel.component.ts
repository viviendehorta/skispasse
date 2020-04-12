import { Component, OnInit } from '@angular/core';
import { ROLE_ADMIN } from 'app/shared/constants/role.constants';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'skis-admin-panel',
  templateUrl: './admin-panel.component.html',
  styleUrls: ['./admin-panel.component.scss']
})
export class AdminPanelComponent implements OnInit {
  constructor(private accountService: AccountService) {}

  ngOnInit() {}

  isAdmin() {
    return this.accountService.getRole() === ROLE_ADMIN;
  }
}
