import {HttpHeaders} from "@angular/common/http"

export class HttpUtils {

    static JSON_REQUEST_HEADER_OPTIONS = {headers: new HttpHeaders({
            "Accept": "application/problem+json",
            "Content-Type": "application/json"})}
    static URL_ENCODED_CONTENT_TYPE_OPTIONS = {headers: new HttpHeaders({
            "Content-Type": "application/x-www-form-urlencoded"
        })}
}
