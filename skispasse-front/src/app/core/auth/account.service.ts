import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {Observable, of, ReplaySubject} from 'rxjs';
import {catchError, map, shareReplay, tap} from 'rxjs/operators';
import {environment} from "../../../environments/environment";
import {StateStorageService} from "./state-storage.service";
import {UserAccount} from "../../shared/model/account.model";
import {AuthenticationState} from "../../shared/model/authentication-state.model";


@Injectable({ providedIn: 'root' })
export class AccountService {

    private resourceUrl = environment.serverUrl + 'account';

    private userAccount: UserAccount | null = null;
    private authenticationState = new ReplaySubject<UserAccount | null>(1);
    private accountCache$?: Observable<UserAccount | null>;

    constructor(private http: HttpClient, private stateStorageService: StateStorageService, private router: Router) {}

    updateAccount(account: UserAccount): Observable<{}> {
        return this.http.post(this.resourceUrl, account);
    }

    updatePassword(newPassword: string, currentPassword: string): Observable<any> {
        return this.http.post(this.resourceUrl + '/change-password', { currentPassword, newPassword });
    }

    getAccount(force?: boolean): Observable<UserAccount | null> {
        if (!this.accountCache$ || force || !this.isAuthenticated()) {
            this.accountCache$ = this.fetchAccount().pipe(
                catchError(() => {
                    return of(null);
                }),
                tap((userAccount: UserAccount | null) => {
                    this.authenticate(userAccount);

                    if (userAccount) {
                        this.navigateToStoredUrl();
                    }
                }),
                shareReplay()
            );
        }
        return this.accountCache$;
    }

    fetchAccount(): Observable<UserAccount> {
        return this.http.get<AuthenticationState>(this.resourceUrl + '/authentication').pipe(
            map(authenticationState => authenticationState.user)
        );
    }

    authenticate(identity: UserAccount | null): void {
        this.userAccount = identity;
        this.authenticationState.next(this.userAccount);
    }

    hasAnyAuthority(authorities: string[] | string): boolean {
        if (!this.userAccount || !this.userAccount.authorities) {
            return false;
        }
        if (!Array.isArray(authorities)) {
            authorities = [authorities];
        }
        return this.userAccount.authorities.some((authority: string) => authorities.includes(authority));
    }

    isAuthenticated(): boolean {
        return this.userAccount !== null;
    }

    /**
     * Only used once from account panel component and else use getAccount() method because don't understand this part of JHipster code, doing same
     */
    getAuthenticationState(): Observable<UserAccount | null> {
        return this.authenticationState.asObservable();
    }

    private navigateToStoredUrl(): void {
        // previousState can be set in the authExpiredInterceptor and in the userRouteAccessService
        // if login is successful, go to stored previousState and clear previousState
        const previousUrl = this.stateStorageService.getUrl();
        if (previousUrl) {
            this.stateStorageService.clearUrl();
            this.router.navigateByUrl(previousUrl);
        }
    }
}
