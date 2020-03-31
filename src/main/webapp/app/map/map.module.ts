import { NgModule } from '@angular/core';
import { NewsCategoryCheckboxComponent } from './news-category-checkbox/news-category-checkbox.component';
import { NewsFactDetailModalContentComponent } from './news-fact-detail-modal/news-fact-detail-modal.content.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';

@NgModule({
  declarations: [NewsCategoryCheckboxComponent, NewsFactDetailModalContentComponent],
  imports: [FontAwesomeModule, NgbModule, CommonModule],
  exports: [NewsCategoryCheckboxComponent, NewsFactDetailModalContentComponent]
})
export class MapModule {}
