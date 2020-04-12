import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SkispasseSharedModule } from 'app/shared/shared.module';
import { WORLDMAP_ROUTE } from './worldmap.route';
import { WorldmapComponent } from './worldmap.component';
import { NewsCategoryCheckboxComponent } from 'app/worldmap/news-category-checkbox/news-category-checkbox.component';
import { NewsFactDetailModalContentComponent } from 'app/worldmap/news-fact-detail-modal/news-fact-detail-modal.content.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { AccountPanelComponent } from 'app/worldmap/account-panel/account-panel.component';
import { AdminPanelComponent } from 'app/worldmap/admin-panel/admin-panel.component';
import { NewsCategoryPanelComponent } from 'app/worldmap/news-category-panel/news-category-panel.component';

@NgModule({
  declarations: [
    AccountPanelComponent,
    AdminPanelComponent,
    NewsCategoryPanelComponent,
    NewsCategoryCheckboxComponent,
    NewsFactDetailModalContentComponent,
    WorldmapComponent
  ],
  exports: [NewsCategoryCheckboxComponent, NewsFactDetailModalContentComponent],
  imports: [CommonModule, FontAwesomeModule, NgbModule, RouterModule.forChild([WORLDMAP_ROUTE]), SkispasseSharedModule]
})
export class SkispasseWorldmapModule {}
