import { Component, EventEmitter, Input, Output, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'skis-news-category-checkbox',
  templateUrl: './news-category-checkbox.component.html',
  styleUrls: ['news-category-checkbox.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NewsCategoryCheckboxComponent {
  @Input() newsCategory: any;
  @Output() categoryChangedEmitter = new EventEmitter<{ categoryValue: string; isSelected: boolean }>();

  emitCategoryChanged($event) {
    this.categoryChangedEmitter.emit({
      categoryValue: $event.target.value,
      isSelected: $event.target.checked
    });
  }
}
