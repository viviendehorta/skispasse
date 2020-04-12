import { Injectable } from '@angular/core';
import { NewsCategory } from 'app/shared/beans/news-category.model';

@Injectable({ providedIn: 'root' })
export class NewsCategorySelectionService {
  private newsCategories: NewsCategory[];

  constructor() {}

  setNewsCategories(newsCategories: NewsCategory[]) {
    this.newsCategories = newsCategories;
  }

  setCategorySelection(categoryId: number, isSelected: boolean) {
    const category = this.newsCategories.find(newsCategory => newsCategory.id === categoryId);
    category.isSelected = isSelected;
  }

  getSelectedNewsCategoryIds(): number[] {
    return this.newsCategories.filter(category => category.isSelected).map(category => category.id);
  }
}
