import { Component, Input, ViewEncapsulation } from '@angular/core';
import { NewsCategorySelection } from 'app/shared/beans/news-category-selection.model';
import { JhiEventManager } from 'ng-jhipster';

@Component({
  selector: 'skis-news-category-checkbox',
  templateUrl: './news-category-checkbox.component.html',
  styleUrls: ['news-category-checkbox.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NewsCategoryCheckboxComponent {
  @Input() newsCategorySelection: NewsCategorySelection;

  constructor(private eventManager: JhiEventManager) {}

  emitCategoryChanged($event) {
    this.eventManager.broadcast({
      name: 'newsCategorySelectionChanged',
      content: {
        categoryId: $event.target.value,
        isSelected: $event.target.checked
      }
    });
  }
}
