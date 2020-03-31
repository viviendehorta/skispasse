import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class NewsFactService {
  BASE_URL = SERVER_API_URL + 'newsFacts/';

  allNewsFacts: any[];

  constructor(private http: HttpClient) {}

  fetchNewsFacts(): Observable<Object> {
    return this.http.post(this.BASE_URL + 'all', {});
  }

  getAll() {
    return this.allNewsFacts;
  }

  getNewsFactDetail(newsFactId: number): Observable<Object> {
    return this.http.post(this.BASE_URL + newsFactId, {});
  }

  getFilteredByCategoryIds(categoryIds: number[]) {
    return this.allNewsFacts.filter(newsFact => categoryIds.includes(newsFact.categoryId));
  }

  flattenNewsFacts(unFlattenedNewsFacts: Object): void {
    this.allNewsFacts = unFlattenedNewsFacts as any[];
  }

  flattenNewsFactDetail(unFlattenedNewsFactDetail: Object) {
    return unFlattenedNewsFactDetail as { address: string; city: string; country: string };
  }
}
