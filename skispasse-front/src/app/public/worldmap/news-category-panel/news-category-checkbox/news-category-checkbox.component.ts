import {Component, Input, ViewEncapsulation} from '@angular/core';
import {NewsCategorySelection} from '../../../../shared/model/news-category-selection.model';
import {EventManager} from '../../../../core/events/event-manager';
import {SMALL_PNG_BY_NEWS_CATEGORY} from "../../../../core/map/marker-style.constants";

@Component({
  selector: 'skis-news-category-checkbox',
  templateUrl: './news-category-checkbox.component.html',
  encapsulation: ViewEncapsulation.None
})
export class NewsCategoryCheckboxComponent {
  @Input() newsCategorySelection: NewsCategorySelection;

  constructor(private eventManager: EventManager) {}

  emitCategoryChanged($event) {
    this.eventManager.broadcast({
      name: 'newsCategorySelectionChanged',
      content: {
        categoryId: $event.target.value,
        isSelected: $event.target.checked
      }
    });
  }

  getNewsCategoryMarkerIcon(newsCategoryId: string) {
    return SMALL_PNG_BY_NEWS_CATEGORY[newsCategoryId];
  }
}
