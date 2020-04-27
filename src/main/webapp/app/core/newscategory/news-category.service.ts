import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { NewsCategory } from 'app/shared/model/news-category.model';

@Injectable({ providedIn: 'root' })
export class NewsCategoryService {
  private BASE_URL = SERVER_API_URL + 'newsCategory/';

  constructor(private http: HttpClient) {}

  fetchCategories() {
    return this.http.get(this.BASE_URL + 'all', {});
  }

  flattenNewsCategories(unFlattenedNewsCategories: Object) {
    return unFlattenedNewsCategories as NewsCategory[];
  }
}
