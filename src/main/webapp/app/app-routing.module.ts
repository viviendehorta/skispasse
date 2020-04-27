import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute } from './shared/error/error.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { WorldmapComponent } from 'app/public/worldmap/worldmap.component';
import { ROLE_ADMIN, ROLE_ANONYMOUS, ROLE_CONTRIBUTOR, ROLE_USER } from 'app/shared/constants/role.constants';

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
      { enableTracing: DEBUG_INFO_ENABLED }
    )
  ],
  exports: [RouterModule]
})
export class SkispasseAppRoutingModule {}
