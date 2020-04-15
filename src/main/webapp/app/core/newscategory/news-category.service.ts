import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { NewsCategory } from 'app/shared/beans/news-category.model';

@Injectable({ providedIn: 'root' })
export class NewsCategoryService {
  private BASE_URL = SERVER_API_URL + 'newsCategory/';

  constructor(private http: HttpClient) {}

  fetchCategories() {
    return this.http.post(this.BASE_URL + 'all', {});
  }

  flattenNewsCategories(unFlattenedNewsCategories: Object) {
    const newsCategoriesFromBack = unFlattenedNewsCategories as { id: number; label: string }[];
    return newsCategoriesFromBack.map(backNewsCategory => this.backToFrontBean(backNewsCategory));
  }

  private backToFrontBean(bean: { id: number; label: string }): NewsCategory {
    return {
      id: bean.id,
      label: bean.label,
      isSelected: false
    };
  }
}
