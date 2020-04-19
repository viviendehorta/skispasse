import { NgModule } from '@angular/core';

import { SkispasseSharedModule } from 'app/shared/shared.module';
import { WorldmapComponent } from './worldmap.component';
import { NewsCategoryCheckboxComponent } from 'app/worldmap/news-category-checkbox/news-category-checkbox.component';
import { NewsFactDetailModalContentComponent } from 'app/worldmap/news-fact-detail-modal/news-fact-detail-modal.content.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { AccountPanelComponent } from 'app/worldmap/account-panel/account-panel.component';
import { AdminPanelComponent } from 'app/worldmap/admin-panel/admin-panel.component';
import { NewsCategoryPanelComponent } from 'app/worldmap/news-category-panel/news-category-panel.component';
import { ContributorPanelComponent } from 'app/worldmap/contributor-panel/contributor-panel.component';
import { RouterModule } from '@angular/router';

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
  imports: [CommonModule, FontAwesomeModule, NgbModule, SkispasseSharedModule, RouterModule]
})
export class SkispasseWorldmapModule {}
