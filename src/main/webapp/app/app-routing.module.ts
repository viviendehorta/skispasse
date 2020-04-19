import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute } from './layouts/error/error.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { WorldmapComponent } from 'app/worldmap/worldmap.component';
import { NewsfactManagementComponent } from 'app/contrib/newsfact-management/newsfact-management.component';
import { ROLE_CONTRIBUTOR } from 'app/shared/constants/role.constants';

const LAYOUT_ROUTES = [...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: '',
          component: WorldmapComponent,
          data: {
            authorities: [],
            pageTitle: 'worldmap.title'
          }
        },
        {
          path: 'admin',
          data: {
            authorities: ['ROLE_ADMIN']
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule)
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule)
        },
        {
          path: 'contribute',
          component: NewsfactManagementComponent,
          data: {
            authorities: [ROLE_CONTRIBUTOR],
            pageTitle: 'newsFactManagement.title'
          }
        },
        ...LAYOUT_ROUTES
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    )
  ],
  exports: [RouterModule]
})
export class SkispasseAppRoutingModule {}
