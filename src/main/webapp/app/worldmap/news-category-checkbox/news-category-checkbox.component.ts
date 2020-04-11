import { Component, EventEmitter, Input, Output, ViewEncapsulation } from '@angular/core';
import { NewsCategory } from 'app/shared/beans/news-category.model';

@Component({
  selector: 'skis-news-category-checkbox',
  templateUrl: './news-category-checkbox.component.html',
  styleUrls: ['news-category-checkbox.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NewsCategoryCheckboxComponent {
  @Input() newsCategory: NewsCategory;
  @Output() categoryChangedEmitter = new EventEmitter<{ categoryId: number; isSelected: boolean }>();

  emitCategoryChanged($event) {
    this.categoryChangedEmitter.emit({
      categoryId: parseInt($event.target.value, 10),
      isSelected: $event.target.checked
    });
  }
}
