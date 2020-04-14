import { Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { SettingsComponent } from 'app/account/settings/settings.component';

export const accountRoute: Routes = [
  {
    path: '',
    children: [
      {
        path: 'settings',
        component: SettingsComponent,
        data: {
          authorities: ['ROLE_USER'],
          pageTitle: 'account.settings.title'
        },
        canActivate: [UserRouteAccessService]
      }
    ]
  }
];
