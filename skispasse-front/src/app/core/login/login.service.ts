import {Injectable} from '@angular/core';
import {flatMap, map} from 'rxjs/operators';
import {AccountService} from '../auth/account.service';
import {Observable} from "rxjs";
import {Login} from "./login.model";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {environment} from "../../../environments/environment";
import {UserAccount} from "../../shared/model/account.model";

@Injectable({providedIn: 'root'})
export class LoginService {

    private resourceUrl = environment.serverUrl + 'account/';
    private logoutUrl = this.resourceUrl + 'logout';

    constructor(private http: HttpClient, private accountService: AccountService) {
    }

    login(credentials: Login): Observable<UserAccount | null> {
        const loginData =
            `username=${encodeURIComponent(credentials.username)}` +
            `&password=${encodeURIComponent(credentials.password)}` +
            `&remember-me=${credentials.rememberMe}` +
            '&submit=Login';

        const headers = new HttpHeaders().set('Content-Type', 'application/x-www-form-urlencoded');

        return this.http.post(this.resourceUrl + 'login', loginData, {headers}).pipe(
            flatMap(() => this.accountService.getAccount(true)));
    }

    logout(): void {
        this.http.post(this.getLogoutUrl(), {}).pipe(
            map(() => {
                // to get a new csrf token, fetch remote account
                this.accountService.fetchAccount().subscribe();
            })
        ).subscribe(null, null, () => this.accountService.authenticate(null));
    }

    getLogoutUrl(): string {
        return this.logoutUrl;
    }

    logoutInClient(): void {
        this.accountService.authenticate(null);
    }
}
