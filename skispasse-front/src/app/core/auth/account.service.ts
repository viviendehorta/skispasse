import {Injectable} from '@angular/core';
import {SessionStorageService} from 'ngx-webstorage';
import {HttpClient} from '@angular/common/http';
import {Observable, of, Subject} from 'rxjs';
import {catchError, shareReplay, tap} from 'rxjs/operators';
import {Account} from '../../shared/model/account.model';
import {environment} from '../../../environments/environment';


@Injectable({ providedIn: 'root' })
export class AccountService {
  private resourceUrl = environment.serverUrl + 'account';
  private userIdentity: Account;
  private authenticated = false;
  private authenticationState = new Subject<any>();
  private accountCache$: Observable<Account>;

  constructor(
    private sessionStorage: SessionStorageService,
    private http: HttpClient) {}

  fetch(): Observable<Account> {
    return this.http.get<Account>(this.resourceUrl);
  }

  update(account: Account): Observable<Account> {
    return this.http.put<Account>(this.resourceUrl, account);
  }

  authenticate(identity) {
    this.userIdentity = identity;
    this.authenticated = identity !== null;
    this.authenticationState.next(this.userIdentity);
  }

  hasAnyAuthority(authorities: string[] | string): boolean {
    if (!this.authenticated || !this.userIdentity || !this.userIdentity.authorities) {
      return false;
    }

    if (!Array.isArray(authorities)) {
      authorities = [authorities];
    }

    return authorities.some((authority: string) => this.userIdentity.authorities.includes(authority));
  }

  identity(force?: boolean): Observable<Account> {
    if (force || !this.authenticated) {
      this.accountCache$ = null;
    }

    if (!this.accountCache$) {
      this.accountCache$ = this.fetch().pipe(
        catchError(() => {
          return of(null);
        }),
        tap(account => {
          if (account) {
            this.userIdentity = account;
            this.authenticated = true;
          } else {
            this.userIdentity = null;
            this.authenticated = false;
          }
          this.authenticationState.next(this.userIdentity);
        }),
        shareReplay()
      );
    }
    return this.accountCache$;
  }

  isAuthenticated(): boolean {
    return this.authenticated;
  }

  isIdentityResolved(): boolean {
    return this.userIdentity !== undefined;
  }

  getAuthenticationState(): Observable<any> {
    return this.authenticationState.asObservable();
  }
}
