import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute } from './shared/error/error.route';

import { UserRouteAccessService } from './core/auth/user-route-access-service';
import { WorldmapComponent } from './public/worldmap/worldmap.component';
import { ROLE_ADMIN, ROLE_CONTRIBUTOR, ROLE_USER } from './shared/constants/role.constants';
import {environment} from '../environments/environment';

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
            pageTitle: 'World Map'
          }
        },
        {
          path: 'admin',
          data: {
            authorities: [ROLE_ADMIN]
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule)
        },
        {
          path: 'contrib',
          data: {
            authorities: [ROLE_CONTRIBUTOR]
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./contrib/contrib-routing.module').then(m => m.ContribRoutingModule)
        },
        {
          path: 'public',
          loadChildren: () => import('./public/public-routing.module').then(m => m.PublicRoutingModule)
        },
        {
          path: 'user',
          data: {
            authorities: [ROLE_USER]
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./user/user-routing.module').then(m => m.UserRoutingModule)
        },
        ...LAYOUT_ROUTES
      ],
      { enableTracing: environment.debugInfoEnabled }
    )
  ],
  exports: [RouterModule]
})
export class SkispasseAppRoutingModule {}
