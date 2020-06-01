import {Injectable} from '@angular/core';
import {tap} from 'rxjs/operators';
import {AccountService} from '../auth/account.service';
import {AuthServerProvider} from '../auth/auth-session.service';
import {Observable} from "rxjs";

@Injectable({providedIn: 'root'})
export class LoginService {
    constructor(private accountService: AccountService, private authServerProvider: AuthServerProvider) {
    }

    login(credentials): Observable<any> {
        console.log("LoginService : enter login");
        this.accountService.clearAuthentication();
        return this.authServerProvider.login(credentials).pipe(
            tap((loginResult: any) => {
                console.log("LoginService : received login result= " + JSON.stringify(loginResult));
                console.log("LoginService : calling accountService.fetchAuthenticationState()");
                this.accountService.getAuthenticationState();
            })
        );
    }

    logout(): void {
        this.authServerProvider.logout().subscribe(null, null, () => this.accountService.clearAuthentication());
    }
}
