import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { NewsFactNoDetail } from 'app/shared/beans/news-fact-no-detail.model';
import { NewsFactDetail } from 'app/shared/beans/news-fact-detail.model';
import { createHttpPagingOption } from 'app/shared/util/request-util';

@Injectable({ providedIn: 'root' })
export class NewsFactService {
  private BASE_URL = SERVER_API_URL + 'newsFact/';

  constructor(private http: HttpClient) {}

  fetchNewsFacts(): Observable<Object> {
    return this.http.post(this.BASE_URL + 'all', {});
  }

  getNewsFactDetail(newsFactId: number): Observable<Object> {
    return this.http.post(this.BASE_URL + newsFactId, {});
  }

  filterByCategoryIds(newsFacts: NewsFactNoDetail[], categoryIds: number[]) {
    return newsFacts.filter(newsFact => categoryIds.includes(newsFact.categoryId));
  }

  getByUser(userLogin: string, pagingParams?: any): Observable<HttpResponse<NewsFactDetail[]>> {
    const options = createHttpPagingOption(pagingParams);
    return this.http.get<NewsFactDetail[]>(this.BASE_URL + 'byUser/' + userLogin, { params: options, observe: 'response' });
  }

  flattenNewsFacts(unFlattenedNewsFacts: Object): NewsFactNoDetail[] {
    return unFlattenedNewsFacts as NewsFactNoDetail[];
  }

  flattenNewsFactDetail(unFlattenedNewsFactDetail: Object): NewsFactDetail {
    return unFlattenedNewsFactDetail as NewsFactDetail;
  }
}