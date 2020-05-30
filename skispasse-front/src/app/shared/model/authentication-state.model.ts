import {UserAccount} from './account.model';

export interface AuthenticationState {
    authenticated: boolean,
    user: UserAccount
}
