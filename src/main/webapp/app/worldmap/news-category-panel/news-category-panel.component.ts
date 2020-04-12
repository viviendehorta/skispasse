import { Component, OnInit } from '@angular/core';
import { NewsCategory } from 'app/shared/beans/news-category.model';
import { NewsCategoryService } from 'app/core/newscategory/news-category.service';
import { NewsCategorySelectionService } from 'app/worldmap/news-category-selection.service';

@Component({
  selector: 'skis-news-category-panel',
  templateUrl: './news-category-panel.component.html',
  styleUrls: ['./news-category-panel.component.scss']
})
export class NewsCategoryPanelComponent implements OnInit {
  newsCategories: NewsCategory[];

  constructor(private newsCategoryService: NewsCategoryService, private newsCategorySelectionService: NewsCategorySelectionService) {
    this.newsCategories = [];
  }

  ngOnInit() {
    this.newsCategoryService.fetchCategories().subscribe(unflattenedNewsCategories => {
      const flattenedNewsCategories = this.newsCategoryService.flattenNewsCategories(unflattenedNewsCategories);
      this.newsCategories = flattenedNewsCategories;
      this.newsCategorySelectionService.setNewsCategories(flattenedNewsCategories);
    });
  }
}
