import { Routes } from '@angular/router';
import { SettingsComponent } from 'app/user/settings/settings.component';

export const settingsRoute: Routes = [
  {
    path: '',
    component: SettingsComponent,
    data: {
      pageTitle: 'account.settings.title'
    }
  }
];
