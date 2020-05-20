import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from '../../shared/model/user.model';

@Component({
  selector: 'skis-user-mgmt-detail',
  templateUrl: './user-management-detail.component.html'
})
export class UserManagementDetailComponent implements OnInit {
  user: User;

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.data.subscribe(({ user }) => {
      this.user = user.body ? user.body : user;
    });
  }
}
