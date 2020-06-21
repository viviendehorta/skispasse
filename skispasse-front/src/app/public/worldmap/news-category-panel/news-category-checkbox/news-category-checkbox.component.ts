import {Component, Input, ViewEncapsulation} from '@angular/core';
import {NewsCategorySelection} from '../../../../shared/model/news-category-selection.model';
import {EventManager} from '../../../../core/events/event-manager';
import {PNG_BY_NEWS_CATEGORY} from "../../../../core/map/marker-style.constants";

@Component({
  selector: 'skis-news-category-checkbox',
  templateUrl: './news-category-checkbox.component.html',
  styleUrls: ['news-category-checkbox.scss'],
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
    return PNG_BY_NEWS_CATEGORY[newsCategoryId];
  }
}
