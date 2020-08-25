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

    getNewsFactImageString(newsFactId: string) {
        return "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";
    }
}
