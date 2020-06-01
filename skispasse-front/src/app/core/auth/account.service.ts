import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {UserAccount} from '../../shared/model/account.model';
import {environment} from '../../../environments/environment';
import {AuthenticationState} from "../../shared/model/authentication-state.model";


@Injectable({providedIn: 'root'})
export class AccountService {

    private UNAUTHENTICATED_STATE: AuthenticationState = {authenticated: false, user: null};

    private resourceUrl = environment.serverUrl + 'account';

    private hasExpiredAuthentication: boolean;
    private authenticationStateSubject: BehaviorSubject<AuthenticationState>;

    constructor(private http: HttpClient) {
        this.hasExpiredAuthentication = true;
        this.authenticationStateSubject = new BehaviorSubject<AuthenticationState>(this.UNAUTHENTICATED_STATE);
        this.logHere("End Constructor");
    }

    updateAccount(account: UserAccount): Observable<UserAccount> {
        return this.http.put<UserAccount>(this.resourceUrl + '/', account);
    }

    updatePassword(newPassword: string, currentPassword: string): Observable<any> {
        return this.http.post(this.resourceUrl + '/change-password', {currentPassword, newPassword});
    }

    /**
     * Fetch the authentication state from the server and notify the subscribers with the received value.
     * If called from a none-anonymous accessible context, better to use fetchAuthenticatedAccount() that returns
     * directly the AuthenticatedState.user
     */
    getAuthenticationState(): Observable<AuthenticationState> {
        this.logHere("enter getAuthenticationState");
        if (this.hasExpiredAuthentication) {
            this.logHere("going to fetch authenticationState");
            this.http.get<AuthenticationState>(this.resourceUrl + '/authentication').subscribe(authenticationState => {
                this.logHere("Receive server autthentication state : " + JSON.stringify(authenticationState));
                this.logHere("Calling next on authentication subject");
                this.authenticationStateSubject.next(authenticationState);
                this.hasExpiredAuthentication = false;
            });
        }
        return this.authenticationStateSubject.asObservable();
    }

    /**
     * Clear authentication data so that, for the front, user is disconnected.
     * Should only be called for logging out, otherwise, frontend auhentication state will be desynchronized with backend
     */
    clearAuthentication(): void {
        this.logHere("enter clearAuthentication");
        this.hasExpiredAuthentication = true;
        this.authenticationStateSubject.next(this.UNAUTHENTICATED_STATE);
    }

    isAuthenticated(): boolean {
        return !this.hasExpiredAuthentication && this.authenticationStateSubject.getValue().authenticated;
    }

    hasAnyAuthority(roles: string[] | string): boolean {
        if (this.hasExpiredAuthentication || !this.authenticationStateSubject.getValue().authenticated || !this.authenticationStateSubject.getValue().user.authorities) {
            return false;
        }

        if (!Array.isArray(roles)) {
            roles = [roles];
        }

        return roles.some((role: string) => this.authenticationStateSubject.getValue().user.authorities.includes(role));
    }

    private logHere(msg: string) {
        console.log("AccountService : " + msg);
    }
}
