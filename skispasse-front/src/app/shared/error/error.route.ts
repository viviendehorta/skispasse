import { Routes } from '@angular/router';

import { ErrorComponent } from './error.component';

export const errorRoute: Routes = [
  {
    path: 'error',
    component: ErrorComponent,
    data: {
      authorities: [],
      pageTitle: 'Error'
    }
  },
  {
    path: 'access-denied',
    component: ErrorComponent,
    data: {
      authorities: [],
      pageTitle: 'Access denied',
      error403: true
    }
  },
  {
    path: '404',
    component: ErrorComponent,
    data: {
      authorities: [],
      pageTitle: 'Not found',
      error404: true
    }
  },
  {
    path: '**',
    redirectTo: '/404'
  }
];
