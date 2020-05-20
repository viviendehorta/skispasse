import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'my-news-facts',
        loadChildren: () => import('./my-news-facts/my-news-facts.module').then(m => m.MyNewsFactsModule)
      }
    ])
  ]
})
export class ContribRoutingModule {}
