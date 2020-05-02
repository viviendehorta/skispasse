import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, Routes } from '@angular/router';
import { JhiResolvePagingParams } from 'ng-jhipster';

import { User } from 'app/shared/model//user.model';
import { UserService } from 'app/core/user/user.service';
import { UserManagementComponent } from './user-management.component';
import { UserManagementDetailComponent } from './user-management-detail.component';
import { UserManagementUpdateComponent } from './user-management-update.component';

@Injectable({ providedIn: 'root' })
export class UserManagementResolve implements Resolve<any> {
  constructor(private service: UserService) {}

  resolve(route: ActivatedRouteSnapshot) {
    const login = route.params['login'] ? route.params['login'] : null;
    if (login) {
      return this.service.find(login);
    }
    return new User();
  }
}

export const userManagementRoutes: Routes = [
  {
    path: '',
    component: UserManagementComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      pageTitle: 'userManagement.list.title',
      defaultSort: 'id,asc'
    }
  },
  {
    path: ':login/view',
    component: UserManagementDetailComponent,
    resolve: {
      user: UserManagementResolve
    },
    data: {
      pageTitle: 'userManagement.detail.title'
    }
  },
  {
    path: 'new',
    component: UserManagementUpdateComponent,
    data: {
      pageTitle: 'userManagement.creation.title'
    },
    resolve: {
      user: UserManagementResolve
    }
  },
  {
    path: ':login/edit',
    component: UserManagementUpdateComponent,
    data: {
      pageTitle: 'userManagement.edition.title'
    },
    resolve: {
      user: UserManagementResolve
    }
  }
];
