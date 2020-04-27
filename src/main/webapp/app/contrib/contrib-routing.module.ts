import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'my-news-facts',
        loadChildren: () => import('./published-news-facts/published-news-facts.module').then(m => m.PublishedNewsFactsModule)
      }
    ])
  ]
})
export class ContribRoutingModule {}
