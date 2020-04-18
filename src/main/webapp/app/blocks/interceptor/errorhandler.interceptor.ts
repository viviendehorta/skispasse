import { Injectable } from '@angular/core';
import { JhiEventManager } from 'ng-jhipster';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpHeaders, HttpInterceptor, HttpRequest } from '@angular/common/http';
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
              let errorMessageField = err.error;

              /* Override the 'error' field written in english by the i18n matching key */
              if (err.status === 504) {
                errorMessageField = 'error.http.504';
              }

              const errorCopy = new HttpErrorResponse({
                error: errorMessageField,
                headers: err.headers,
                status: err.status,
                statusText: err.statusText,
                url: err.url
              });

              this.eventManager.broadcast({ name: 'skispasseApp.httpError', content: errorCopy });
            }
          }
        }
      )
    );
  }
}
