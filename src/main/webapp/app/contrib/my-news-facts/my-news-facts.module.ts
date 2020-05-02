import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SkispasseSharedModule } from 'app/shared/shared.module';
import { myNewsFactsRoutes } from 'app/contrib/my-news-facts/my-news-facts.routes';
import { MyNewsFactsComponent } from 'app/contrib/my-news-facts/my-news-facts.component';
import { MyNewsFactEditionComponent } from 'app/contrib/my-news-facts/my-news-fact-edition.component';
import { DeleteNewsFactDialogComponent } from 'app/contrib/my-news-facts/delete-news-fact-dialog.component';

@NgModule({
  declarations: [DeleteNewsFactDialogComponent, MyNewsFactsComponent, MyNewsFactEditionComponent],
  entryComponents: [DeleteNewsFactDialogComponent],
  imports: [RouterModule.forChild(myNewsFactsRoutes), SkispasseSharedModule]
})
export class MyNewsFactsModule {}
