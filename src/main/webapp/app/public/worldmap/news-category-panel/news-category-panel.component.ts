import { Component, OnInit } from '@angular/core';
import { NewsCategoryService } from 'app/core/newscategory/news-category.service';
import { NewsCategorySelectionService } from 'app/core/newscategory/news-category-selection.service';
import { NewsCategorySelection } from 'app/shared/model/news-category-selection.model';

@Component({
  selector: 'skis-news-category-panel',
  templateUrl: './news-category-panel.component.html',
  styleUrls: ['./news-category-panel.component.scss']
})
export class NewsCategoryPanelComponent implements OnInit {
  newsCategorySelections: NewsCategorySelection[];

  constructor(private newsCategoryService: NewsCategoryService, private newsCategorySelectionService: NewsCategorySelectionService) {
    this.newsCategorySelections = [];
  }

  ngOnInit() {
    this.newsCategoryService.fetchCategories().subscribe(unflattenedNewsCategories => {
      const flattenedNewsCategories = this.newsCategoryService.flattenNewsCategories(unflattenedNewsCategories);
      this.newsCategorySelections = this.newsCategorySelectionService.resetNewsCategorySelections(flattenedNewsCategories);
    });
  }
}
