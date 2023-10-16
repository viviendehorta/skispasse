import {Injectable} from '@angular/core';
import {NewsFact} from "../../model/newsfact.model"
import {HttpClient} from "@angular/common/http"
import {API_NEWSFACT_URL} from "../../constants"
import {HttpUtils} from "../util/http-utils"
import {Observable} from "rxjs"

@Injectable({
    providedIn: 'root'
})
export class NewsfactService {

    constructor(private http: HttpClient) {
    }

    list(): Observable<NewsFact[]> {
        return this.http.get<NewsFact[]>(API_NEWSFACT_URL + "/list", HttpUtils.JSON_REQUEST_HEADER_OPTIONS)
    }

}
