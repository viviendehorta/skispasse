import { NgModule } from '@angular/core';

import { WorldmapComponent } from './worldmap.component';
import { RouterModule } from '@angular/router';
import {AccountPanelComponent} from './account-panel/account-panel.component';
import {AdminPanelComponent} from './admin-panel/admin-panel.component';
import {ContributorPanelComponent} from './contributor-panel/contributor-panel.component';
import {NewsCategoryPanelComponent} from './news-category-panel/news-category-panel.component';
import {NewsCategoryCheckboxComponent} from './news-category-panel/news-category-checkbox/news-category-checkbox.component';
import {NewsFactDetailModalContentComponent} from './news-fact-detail-modal/news-fact-detail-modal.content.component';
import {SkispasseSharedModule} from '../../shared/shared.module';

@NgModule({
  declarations: [
    AccountPanelComponent,
    AdminPanelComponent,
    ContributorPanelComponent,
    NewsCategoryPanelComponent,
    NewsCategoryCheckboxComponent,
    NewsFactDetailModalContentComponent,
    WorldmapComponent
  ],
  exports: [NewsCategoryCheckboxComponent, NewsFactDetailModalContentComponent],
  imports: [SkispasseSharedModule, RouterModule]
})
export class WorldmapModule {}
