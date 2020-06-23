import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve, Routes} from '@angular/router';

import {UserManagementComponent} from './user-management.component';
import {UserManagementDetailComponent} from './user-management-detail.component';
import {UserService} from '../../core/user/user.service';
import {User} from '../../shared/model/user.model';
import {ResolvePaginationParamService} from '../../core/pagination/resolve-pagination-param-service';
import {UserEditionComponent} from "./update-user/user-edition.component";
import {UserCreationComponent} from "./create-user/user-creation.component";

@Injectable({ providedIn: 'root' })
export class UserManagementResolve implements Resolve<any> {
  constructor(private service: UserService) {}

  resolve(route: ActivatedRouteSnapshot) {
    const login = route.params.login ? route.params.login : null;
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
      pagingParams: ResolvePaginationParamService
    },
    data: {
      pageTitle: 'Users',
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
      pageTitle: 'User detail'
    }
  },
  {
    path: 'new',
    component: UserCreationComponent,
    data: {
      pageTitle: 'New User'
    },
    resolve: {
      user: UserManagementResolve
    }
  },
  {
    path: ':login/edit',
    component: UserEditionComponent,
    data: {
      pageTitle: 'User edition'
    },
    resolve: {
      user: UserManagementResolve
    }
  }
];
