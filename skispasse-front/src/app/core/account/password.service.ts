import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';


@Injectable({ providedIn: 'root' })
export class PasswordService {
  constructor(private http: HttpClient) {}

  save(newPassword: string, currentPassword: string): Observable<any> {
    return this.http.post(environment.serverUrl + '/account/change-password', { currentPassword, newPassword });
  }
}
