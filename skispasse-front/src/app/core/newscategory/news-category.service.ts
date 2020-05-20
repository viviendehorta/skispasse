import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {NewsCategory} from '../../shared/model/news-category.model';
import {environment} from '../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class NewsCategoryService {
  resourceUrl = environment.serverUrl + 'newsCategory/';

  constructor(private http: HttpClient) {}

  fetchCategories() {
    return this.http.get(this.resourceUrl + 'all', {});
  }

  flattenNewsCategories(unFlattenedNewsCategories: any) {
    return unFlattenedNewsCategories as NewsCategory[];
  }
}
