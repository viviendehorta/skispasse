import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {environment} from '../../../environments/environment';


@Injectable({ providedIn: 'root' })
export class AuthServerProvider {
  private resourceUrl = environment.serverUrl + 'api/';

  constructor(private http: HttpClient) {}

  login(credentials): Observable<any> {
    const data =
      `username=${encodeURIComponent(credentials.username)}` +
      `&password=${encodeURIComponent(credentials.password)}` +
      `&remember-me=${credentials.rememberMe}` +
      '&submit=Login';
    const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');

    return this.http.post(this.resourceUrl + 'authentication', data, { headers });
  }

  logout(): Observable<any> {
    // logout from the server
    return this.http.post(this.resourceUrl + 'logout', {}, { observe: 'response' }).pipe(
      map((response: HttpResponse<any>) => {
        // to get a new csrf token call the api
        this.http.get(this.resourceUrl + 'account').subscribe(() => {}, () => {});
        return response;
      })
    );
  }
}
