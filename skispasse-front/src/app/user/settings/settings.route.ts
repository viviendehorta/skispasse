import { Routes } from '@angular/router';
import {SettingsComponent} from './settings.component';

export const settingsRoute: Routes = [
  {
    path: '',
    component: SettingsComponent,
    data: {
      pageTitle: 'account.settings.title'
    }
  }
];
