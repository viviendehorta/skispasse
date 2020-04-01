import { Route } from '@angular/router';

import { WorldmapComponent } from './worldmap.component';

export const WORLDMAP_ROUTE: Route = {
  path: '',
  component: WorldmapComponent,
  data: {
    authorities: [],
    pageTitle: 'global.title'
  }
};
