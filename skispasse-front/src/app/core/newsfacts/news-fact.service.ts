import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';

import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {LocationCoordinate} from '../../shared/model/location-coordinate.model';
import {INewsFactDetail, NewsFactDetail} from '../../shared/model/news-fact-detail.model';
import {NewsFactNoDetail} from '../../shared/model/news-fact-no-detail.model';
import {createHttpPagingOptions} from '../../shared/util/request-util';
import {PaginationService} from '../pagination/pagination.service';
import {environment} from '../../../environments/environment';
import {INewsFactWithFile} from "../../contrib/my-news-facts/news-fact-form/news-fact-with-file.model";
import {Page} from "../../shared/model/page.model";
import {Media} from "../../shared/model/media.model";

@Injectable({providedIn: 'root'})
export class NewsFactService {
    private resourceUrl = environment.serverUrl + 'newsFacts/';

    constructor(private http: HttpClient, private paginationService: PaginationService) {
    }

    getAll(): Observable<NewsFactNoDetail[]> {
        return this.http.get<any[]>(this.resourceUrl + 'all').pipe(
            map((unparsedNewsFactNoDetails: any[]) => {
                return this.parseNewsFactNoDetails(unparsedNewsFactNoDetails);
            })
        );
    }

    getNewsFactDetail(newsFactId: string): Observable<INewsFactDetail> {
        return this.http.get<INewsFactDetail>(this.resourceUrl + newsFactId).pipe(
            map((unparsedNewsFactDetail: any) => {
                return this.parseNewsFactDetail(unparsedNewsFactDetail);
            })
        );
    }

    getByUser(userLogin: string, pagingParams?: any): Observable<Page> {
        const httpPagingOptions = createHttpPagingOptions(pagingParams);
        return this.http
            .get<any[]>(this.resourceUrl + 'contributor', {
                params: httpPagingOptions,
                observe: 'response'
            })
            .pipe(
                map((httpResponse: HttpResponse<any[]>) => {
                    const newsFactDetails = this.parseNewsFactDetails(httpResponse.body);
                    const itemCount = httpResponse.headers.get('X-Total-Count');
                    const links = this.paginationService.parseHeaderLinks(httpResponse.headers.get('link'));
                    return new Page(newsFactDetails, itemCount, links);
                })
            );
    }

    create(newsFactWithFile: INewsFactWithFile): Observable<INewsFactDetail> {
        const formData = new FormData();
        formData.append("newsFactJson", JSON.stringify(newsFactWithFile.newsFact));
        formData.append("mediaFile", newsFactWithFile.file);

        return this.http.post<INewsFactDetail>(this.resourceUrl, formData).pipe(
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

    getNewsFactVideoUrl(newsFactId: string): string {
        return this.resourceUrl + 'video/' + newsFactId;
    }

    filterByCategoryIds(newsFacts: NewsFactNoDetail[], categoryIds: string[]) {
        return newsFacts.filter(newsFact => categoryIds.includes(newsFact.newsCategoryId));
    }

    parseNewsFactNoDetails(json: any): NewsFactNoDetail[] {
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
            json.media ? new Media(json.media.type, json.media.contentType) : null
        );
    }

    parseNewsFactDetails(jsonList: any[]): NewsFactDetail[] {
        return jsonList.map(jsonItem => this.parseNewsFactDetail(jsonItem));
    }

    getByIds(newsFactIds: string[]) {
        return this.getAll().pipe(
            map((all) => {
              return all.filter(newsFactNoDetail => newsFactIds.includes(newsFactNoDetail.id));
            })
        );
    }
}
