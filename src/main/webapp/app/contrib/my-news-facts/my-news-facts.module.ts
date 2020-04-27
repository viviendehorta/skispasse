import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SkispasseSharedModule } from 'app/shared/shared.module';
import { myNewsFactsRoutes } from 'app/contrib/my-news-facts/my-news-facts.routes';
import { MyNewsFactsComponent } from 'app/contrib/my-news-facts/my-news-facts.component';
import { MyNewsFactEditionComponent } from 'app/contrib/my-news-facts/my-news-fact-edition.component';

@NgModule({
  declarations: [MyNewsFactsComponent, MyNewsFactEditionComponent],
  entryComponents: [],
  imports: [RouterModule.forChild(myNewsFactsRoutes), SkispasseSharedModule]
})
export class MyNewsFactsModule {}
