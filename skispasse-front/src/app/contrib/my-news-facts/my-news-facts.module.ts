import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {SkispasseSharedModule} from '../../shared/shared.module';
import {DeleteNewsFactDialogComponent} from './delete-news-fact-dialog/delete-news-fact-dialog.component';
import {MyNewsFactsComponent} from './my-news-facts.component';
import {myNewsFactsRoutes} from './my-news-facts.routes';
import {NewsFactFormComponent} from "./news-fact-form/news-fact-form.component";
import {NewsFactCreationComponent} from "./create-news-fact/news-fact-creation.component";
import {NewsFactEditionComponent} from "./update-news-fact/news-fact-edition.component";


@NgModule({
  declarations: [DeleteNewsFactDialogComponent, MyNewsFactsComponent, NewsFactCreationComponent, NewsFactEditionComponent, NewsFactFormComponent],
  entryComponents: [DeleteNewsFactDialogComponent],
  imports: [RouterModule.forChild(myNewsFactsRoutes), SkispasseSharedModule]
})
export class MyNewsFactsModule {}
