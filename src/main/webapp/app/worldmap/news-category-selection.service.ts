import { Injectable } from '@angular/core';
import { NewsCategory } from 'app/shared/beans/news-category.model';

@Injectable({ providedIn: 'root' })
export class NewsCategorySelectionService {
  private newsCategoriesSelection: {
    newsCategory: NewsCategory;
    isSelected: boolean;
  }[];

  constructor() {}

  setUnselectedNewsCategories(newsCategories: NewsCategory[]) {
    this.newsCategoriesSelection = newsCategories.map(newsCategory => {
      return {
        newsCategory,
        isSelected: false
      };
    });
  }

  setNewsCategorySelection(categoryId: string, isSelected: boolean) {
    const newsCategoriesSelection = this.newsCategoriesSelection.find(
      newsCategorySelection => newsCategorySelection.newsCategory.id === categoryId
    );
    newsCategoriesSelection.isSelected = isSelected;
  }

  getSelectedNewsCategoryIds(): string[] {
    return this.newsCategoriesSelection
      .filter(newsCategorySelection => newsCategorySelection.isSelected)
      .map(newsCategorySelection => newsCategorySelection.newsCategory.id);
  }
}
