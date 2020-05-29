import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {IUser} from '../../shared/model/user.model';
import {createHttpPagingOptions} from '../../shared/util/request-util';
import {environment} from '../../../environments/environment';


@Injectable({ providedIn: 'root' })
export class UserService {
  resourceUrl = environment.serverUrl + 'users';

  constructor(private http: HttpClient) {}

  create(user: IUser): Observable<IUser> {
    return this.http.post<IUser>(this.resourceUrl, user);
  }

  update(user: IUser): Observable<IUser> {
    return this.http.put<IUser>(this.resourceUrl, user);
  }

  find(login: string): Observable<IUser> {
    return this.http.get<IUser>(`${this.resourceUrl}/${login}`);
  }

  query(pageRequest?: any): Observable<HttpResponse<IUser[]>> {
    const options = createHttpPagingOptions(pageRequest);
    return this.http.get<IUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(login: string): Observable<any> {
    return this.http.delete(`${this.resourceUrl}/${login}`);
  }

  authorities(): Observable<string[]> {
    return this.http.get<string[]>(this.resourceUrl + '/authorities');
  }
}
