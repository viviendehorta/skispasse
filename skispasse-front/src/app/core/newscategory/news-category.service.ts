import {Observable} from "rxjs";
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NewsCategory} from '../../shared/model/news-category.model';
import {environment} from '../../../environments/environment';
import {map} from "rxjs/operators";

@Injectable({providedIn: 'root'})
export class NewsCategoryService {
    resourceUrl = environment.serverUrl + 'newsCategories/';

    constructor(private http: HttpClient) {
    }

    fetchNewsCategories(): Observable<NewsCategory[]> {
        return this.http.get(this.resourceUrl + 'all', {}).pipe(
            map((unparsedNewsCategories: any[]) => (unparsedNewsCategories as NewsCategory[]))
        );
    }
}
