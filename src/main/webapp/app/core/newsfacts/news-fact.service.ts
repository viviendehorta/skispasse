import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { NewsFactNoDetail } from 'app/shared/model/news-fact-no-detail.model';
import { NewsFactDetail } from 'app/shared/model/news-fact-detail.model';
import { createHttpPagingOptions } from 'app/shared/util/request-util';

@Injectable({ providedIn: 'root' })
export class NewsFactService {
  private BASE_URL = SERVER_API_URL + 'newsFact/';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Object> {
    return this.http.get(this.BASE_URL + 'all');
  }

  getNewsFactDetail(newsFactId: number): Observable<Object> {
    return this.http.get(this.BASE_URL + newsFactId);
  }

  filterByCategoryIds(newsFacts: NewsFactNoDetail[], categoryIds: string[]) {
    return newsFacts.filter(newsFact => categoryIds.includes(newsFact.newsCategoryId));
  }

  getByUser(userLogin: string, pagingParams?: any): Observable<HttpResponse<NewsFactDetail[]>> {
    const httpPagingOptions = createHttpPagingOptions(pagingParams);
    return this.http.get<NewsFactDetail[]>(this.BASE_URL + 'contributor/' + userLogin, { params: httpPagingOptions, observe: 'response' });
  }

  flattenNewsFacts(unFlattenedNewsFacts: Object): NewsFactNoDetail[] {
    return unFlattenedNewsFacts as NewsFactNoDetail[];
  }

  flattenNewsFactDetail(unFlattenedNewsFactDetail: Object): NewsFactDetail {
    return unFlattenedNewsFactDetail as NewsFactDetail;
  }
}
