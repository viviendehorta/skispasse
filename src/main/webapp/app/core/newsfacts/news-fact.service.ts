import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { NewsFactNoDetail } from 'app/shared/model/news-fact-no-detail.model';
import { INewsFactDetail } from 'app/shared/model/news-fact-detail.model';
import { createHttpPagingOptions } from 'app/shared/util/request-util';

@Injectable({ providedIn: 'root' })
export class NewsFactService {
  private resourceUrl = SERVER_API_URL + 'newsFact/';

  constructor(private http: HttpClient) {}

  getAll(): Observable<INewsFactDetail[]> {
    return this.http.get<INewsFactDetail[]>(this.resourceUrl + 'all');
  }

  getNewsFactDetail(newsFactId: number): Observable<INewsFactDetail> {
    return this.http.get<INewsFactDetail>(this.resourceUrl + newsFactId);
  }

  filterByCategoryIds(newsFacts: NewsFactNoDetail[], categoryIds: string[]) {
    return newsFacts.filter(newsFact => categoryIds.includes(newsFact.newsCategoryId));
  }

  getByUser(userLogin: string, pagingParams?: any): Observable<HttpResponse<INewsFactDetail[]>> {
    const httpPagingOptions = createHttpPagingOptions(pagingParams);
    return this.http.get<INewsFactDetail[]>(this.resourceUrl + 'contributor/' + userLogin, {
      params: httpPagingOptions,
      observe: 'response'
    });
  }

  flattenNewsFacts(unFlattenedNewsFacts: Object): NewsFactNoDetail[] {
    return unFlattenedNewsFacts as NewsFactNoDetail[];
  }

  flattenNewsFactDetail(unFlattenedNewsFactDetail: Object): INewsFactDetail {
    return unFlattenedNewsFactDetail as INewsFactDetail;
  }

  create(newsFact: INewsFactDetail): Observable<INewsFactDetail> {
    return this.http.post<INewsFactDetail>(this.resourceUrl, newsFact);
  }

  update(newsFact: INewsFactDetail): Observable<INewsFactDetail> {
    return this.http.put<INewsFactDetail>(this.resourceUrl, newsFact);
  }
}
