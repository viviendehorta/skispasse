import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { errorRoute } from './layouts/error/error.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { WorldmapComponent } from 'app/worldmap/worldmap.component';
import { ROLE_ADMIN, ROLE_CONTRIBUTOR } from 'app/shared/constants/role.constants';

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
          path: 'account',
          loadChildren: () => import('./account/account.module').then(m => m.AccountModule)
        },
        {
          path: 'publication',
          data: {
            authorities: [ROLE_CONTRIBUTOR]
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./publication/publication-routing.module').then(m => m.PublicationRoutingModule)
        },
        ...LAYOUT_ROUTES
      ],
      { enableTracing: DEBUG_INFO_ENABLED }
    )
  ],
  exports: [RouterModule]
})
export class SkispasseAppRoutingModule {}
