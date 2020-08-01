import { Component, OnInit } from '@angular/core';
import {NewsCategoryService} from '../../../core/newscategory/news-category.service';
import {NewsCategorySelection} from '../../../shared/model/news-category-selection.model';
import {NewsCategorySelectionService} from '../../../core/newscategory/news-category-selection.service';

@Component({
  selector: 'skis-news-category-panel',
  templateUrl: './news-category-panel.component.html'
})
export class NewsCategoryPanelComponent implements OnInit {
  newsCategorySelections: NewsCategorySelection[];

  constructor(private newsCategoryService: NewsCategoryService, private newsCategorySelectionService: NewsCategorySelectionService) {
    this.newsCategorySelections = [];
  }

  ngOnInit() {
    this.newsCategoryService.fetchNewsCategories().subscribe(newsCategories => {
      this.newsCategorySelections = this.newsCategorySelectionService.resetNewsCategorySelections(newsCategories);
    });
  }
}
