import {Injectable} from '@angular/core';
import {SessionStorageService} from 'ngx-webstorage';
import {HttpClient} from '@angular/common/http';
import {Observable, Subject} from 'rxjs';
import {map, shareReplay, tap} from 'rxjs/operators';
import {UserAccount} from '../../shared/model/account.model';
import {environment} from '../../../environments/environment';
import {AuthenticationState} from "../../shared/model/authentication-state.model";


@Injectable({providedIn: 'root'})
export class AccountService {
    private resourceUrl = environment.serverUrl + 'account';

    private authenticationState: AuthenticationState;
    private authenticationStateSubject = new Subject<AuthenticationState>(); //Subject used to async request API to get authenticationState when cache is not set
    private authenticationStateCache: Observable<AuthenticationState>;//Cache to not request API each time (TODO : unvalidate it when authentication ticket has expired)

    constructor(
        private sessionStorage: SessionStorageService,
        private http: HttpClient) {
    }

    update(account: UserAccount): Observable<UserAccount> {
        return this.http.put<UserAccount>(this.resourceUrl + '/', account);
    }

    /**
     * Fetch the authentication state from the server if needed.
     * If called from a none-anonymous accessible context, better to use fetchAuthenticatedAccount() that returns
     * directly the AuthenticatedState.user
     */
    fetchAuthenticationState(): Observable<AuthenticationState> {
        if (!this.authenticationState.authenticated) {
            this.authenticationStateCache = null;
        }

        if (!this.authenticationStateCache) {
            this.authenticationStateCache = this.http.get<AuthenticationState>(this.resourceUrl + '/authentication').pipe(
                tap(authenticationState => {
                    this.authenticationState = authenticationState;
                    this.authenticationStateSubject.next(this.authenticationState);
                }),
                shareReplay()
            );
        }
        return this.authenticationStateCache;
    }

    /**
     * Fetch the authentication state account from the server if needed
     * Returns directly the AuthenticatedState.user.
     * Should be called when need to get the authenticated user from a none-anonymous accessible context
     */
    fetchAuthenticatedAccount(): Observable<UserAccount> {
        return this.fetchAuthenticationState().pipe(map(authenticationState => authenticationState.user));
    }

    /**
     * Clear authentication and fetch authenticated state to get updated backend authentication state
     */
    reloadAuthentication() {
        this.clearAuthentication();
        return this.fetchAuthenticationState();
    }

    /**
     * Clear authentication data so that, for the front, user is disconnected.
     * Should only be called for logging out, otherwise, frontend auhentication state will be desynchronized with backend
     */
    clearAuthentication(): void {
        this.authenticationStateCache = null;
        this.authenticationState = null;
        this.authenticationStateSubject.next(this.authenticationState);
    }

    hasAnyAuthority(roles: string[] | string): boolean {
        if (!this.authenticationState.authenticated || !this.authenticationState.user.authorities) {
            return false;
        }

        if (!Array.isArray(roles)) {
            roles = [roles];
        }

        return roles.some((role: string) => this.authenticationState.user.authorities.includes(role));
    }

    isAuthenticated(): boolean {
        return this.authenticationState.authenticated;
    }

    updatePassword(newPassword: string, currentPassword: string): Observable<any> {
        return this.http.post(environment.serverUrl + '/change-password', {currentPassword, newPassword});
    }
}
