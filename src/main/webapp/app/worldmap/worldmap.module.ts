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

@NgModule({
  declarations: [WorldmapComponent, NewsCategoryCheckboxComponent, NewsFactDetailModalContentComponent],
  exports: [NewsCategoryCheckboxComponent, NewsFactDetailModalContentComponent],
  imports: [CommonModule, FontAwesomeModule, NgbModule, RouterModule.forChild([WORLDMAP_ROUTE]), SkispasseSharedModule]
})
export class SkispasseWorldmapModule {}
