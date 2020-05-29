import { Injectable } from '@angular/core';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import {StateStorageService} from '../../core/auth/state-storage.service';
import {LoginService} from '../../core/login/login.service';
import {LoginModalService} from '../../core/login/login-modal.service';


@Injectable()
export class AuthExpiredInterceptor implements HttpInterceptor {
  constructor(
    private loginService: LoginService,
    private loginModalService: LoginModalService,
    private stateStorageService: StateStorageService,
    private router: Router
  ) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap(null, (err: any) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status === 401 && err.url && !err.url.includes('account')) {
            this.stateStorageService.storeUrl(this.router.routerState.snapshot.url);
            this.loginService.logout();
            this.router.navigate(['']);
            this.loginModalService.open();
          }
        }
      })
    );
  }
}
