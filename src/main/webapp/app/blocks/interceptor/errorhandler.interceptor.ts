import { Injectable } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable()
export class ErrorHandlerInterceptor implements HttpInterceptor {
  constructor(private eventManager: JhiEventManager) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap(
        () => {},
        (err: any) => {
          if (err instanceof HttpErrorResponse) {
            if (!(err.status === 401 && err.message === '')) {
              this.eventManager.broadcast({ name: 'skispasseApp.httpError', content: err });
            }
          }
        }
      )
    );
  }
}
