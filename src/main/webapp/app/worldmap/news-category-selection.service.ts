import { Injectable } from '@angular/core';
import { NewsCategory } from 'app/shared/beans/news-category.model';
import { NewsCategorySelection } from 'app/shared/beans/news-category-selection.model';

@Injectable({ providedIn: 'root' })
export class NewsCategorySelectionService {
  private newsCategorySelections: NewsCategorySelection[];

  constructor() {}

  resetNewsCategorySelections(newsCategories: NewsCategory[]): NewsCategorySelection[] {
    this.newsCategorySelections = newsCategories.map(newsCategory => {
      return {
        id: newsCategory.id,
        label: newsCategory.label,
        isSelected: true
      };
    });
    return this.newsCategorySelections;
  }

  setNewsCategorySelection(categoryId: string, isSelected: boolean) {
    const newsCategoriesSelection = this.newsCategorySelections.find(newsCategorySelection => newsCategorySelection.id === categoryId);
    newsCategoriesSelection.isSelected = isSelected;
  }

  getSelectedNewsCategoryIds(): string[] {
    return this.newsCategorySelections
      .filter(newsCategorySelection => newsCategorySelection.isSelected)
      .map(newsCategorySelection => newsCategorySelection.id);
  }
}
