import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import {SkispasseSharedModule} from '../../shared/shared.module';
import {DeleteNewsFactDialogComponent} from './delete-news-fact-dialog.component';
import {MyNewsFactEditionComponent} from './my-news-fact-edition.component';
import {MyNewsFactsComponent} from './my-news-facts.component';
import {myNewsFactsRoutes} from './my-news-facts.routes';


@NgModule({
  declarations: [DeleteNewsFactDialogComponent, MyNewsFactsComponent, MyNewsFactEditionComponent],
  entryComponents: [DeleteNewsFactDialogComponent],
  imports: [RouterModule.forChild(myNewsFactsRoutes), SkispasseSharedModule]
})
export class MyNewsFactsModule {}
