import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SkispasseSharedModule } from 'app/shared/shared.module';
import { publishedNewsFactsRoutes } from 'app/publication/published-news-facts/published-news-facts.routes';
import { PublishedNewsFactsComponent } from 'app/publication/published-news-facts/published-news-facts.component';
import { PublishedNewsFactUpdateComponent } from 'app/publication/published-news-facts/published-news-fact-update.component';

@NgModule({
  imports: [SkispasseSharedModule, RouterModule.forChild(publishedNewsFactsRoutes)],
  declarations: [PublishedNewsFactsComponent, PublishedNewsFactUpdateComponent],
  entryComponents: []
})
export class PublishedNewsFactsModule {}
