import {Injectable} from '@angular/core';
import {flatMap} from 'rxjs/operators';
import {AccountService} from '../auth/account.service';
import {AuthServerProvider} from '../auth/auth-session.service';
import {Observable} from "rxjs";
import {AuthenticationState} from "../../shared/model/authentication-state.model";

@Injectable({providedIn: 'root'})
export class LoginService {
    constructor(private accountService: AccountService, private authServerProvider: AuthServerProvider) {
    }

    login(credentials): Observable<AuthenticationState> {
        return this.authServerProvider.login(credentials).pipe(flatMap(() => {
                return this.accountService.reloadAuthentication();
            }
        ));
    }

    logout(): void {
        this.authServerProvider.logout().subscribe(null, null, () => this.accountService.clearAuthentication());
    }
}
