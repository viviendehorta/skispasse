import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';

import { SERVER_API_URL } from 'app/app.constants';
import { Observable } from 'rxjs';
import { NewsFactNoDetail } from 'app/shared/model/news-fact-no-detail.model';
import { INewsFactDetail, NewsFactDetail } from 'app/shared/model/news-fact-detail.model';
import { createHttpPagingOptions } from 'app/shared/util/request-util';
import { map } from 'rxjs/operators';
import { LocationCoordinate } from 'app/shared/model/location-coordinate.model';
import { JhiParseLinks } from 'ng-jhipster';
import { INewsFactPage, NewsFactPage } from 'app/shared/model/news-fact-page.model';

@Injectable({ providedIn: 'root' })
export class NewsFactService {
  private resourceUrl = SERVER_API_URL + 'newsFact/';

  constructor(private http: HttpClient, private parseLinks: JhiParseLinks) {}

  getAll(): Observable<NewsFactNoDetail[]> {
    return this.http.get<any[]>(this.resourceUrl + 'all').pipe(
      map((unparsedNewsFactNoDetails: any[]) => {
        return this.parseNewsFactNoDetails(unparsedNewsFactNoDetails);
      })
    );
  }

  getNewsFactDetail(newsFactId: number): Observable<INewsFactDetail> {
    return this.http.get<INewsFactDetail>(this.resourceUrl + newsFactId).pipe(
      map((unparsedNewsFactDetail: any) => {
        return this.parseNewsFactDetail(unparsedNewsFactDetail);
      })
    );
  }

  getByUser(userLogin: string, pagingParams?: any): Observable<INewsFactPage> {
    const httpPagingOptions = createHttpPagingOptions(pagingParams);
    return this.http
      .get<any[]>(this.resourceUrl + 'contributor/' + userLogin, {
        params: httpPagingOptions,
        observe: 'response'
      })
      .pipe(
        map((httpResponse: HttpResponse<any[]>) => {
          const newsFactDetails = this.parseNewsFactDetails(httpResponse.body);
          const itemCount = httpResponse.headers.get('X-Total-Count');
          const links = this.parseLinks.parse(httpResponse.headers.get('link'));
          return new NewsFactPage(newsFactDetails, itemCount, links);
        })
      );
  }

  create(newsFact: INewsFactDetail): Observable<INewsFactDetail> {
    return this.http.post<INewsFactDetail>(this.resourceUrl, newsFact).pipe(
      map((unparsedNewsFactDetail: any) => {
        return this.parseNewsFactDetail(unparsedNewsFactDetail);
      })
    );
  }

  delete(newsFactId: string): Observable<any> {
    return this.http.delete(this.resourceUrl + newsFactId);
  }

  update(newsFact: INewsFactDetail): Observable<INewsFactDetail> {
    return this.http.put<INewsFactDetail>(this.resourceUrl, newsFact).pipe(
      map((unparsedNewsFactDetail: any) => {
        return this.parseNewsFactDetail(unparsedNewsFactDetail);
      })
    );
  }

  filterByCategoryIds(newsFacts: NewsFactNoDetail[], categoryIds: string[]) {
    return newsFacts.filter(newsFact => categoryIds.includes(newsFact.newsCategoryId));
  }
  parseNewsFactNoDetails(json: Object): NewsFactNoDetail[] {
    return json as NewsFactNoDetail[];
  }

  parseNewsFactDetail(json: any): INewsFactDetail {
    return new NewsFactDetail(
      json.address,
      json.city,
      json.country,
      json.createdDate ? json.createdDate : null,
      json.eventDate ? json.eventDate : null,
      json.id,
      json.locationCoordinate ? new LocationCoordinate(json.locationCoordinate.x, json.locationCoordinate.y) : null,
      json.newsCategoryId,
      json.newsCategoryLabel,
      json.videoPath
    );
  }

  parseNewsFactDetails(jsonList: any[]): NewsFactDetail[] {
    return jsonList.map(jsonItem => this.parseNewsFactDetail(jsonItem));
  }
}
