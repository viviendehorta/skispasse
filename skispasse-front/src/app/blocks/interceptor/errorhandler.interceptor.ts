import {Injectable} from '@angular/core';
import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';
import {EventManager} from '../../core/events/event-manager';

@Injectable()
export class ErrorHandlerInterceptor implements HttpInterceptor {
  constructor(private eventManager: EventManager) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      tap(
        () => {},
        (err: any) => {
          if (err instanceof HttpErrorResponse) {
            if (this.mustShowError(err)) {
              this.eventManager.broadcast({ name: 'skispasseApp.httpError', content: err });
            }
          }
        }
      )
    );
  }

    private mustShowError(err: HttpErrorResponse) {
        if (err.status === 401 && (err.message === '')) { //User authentication expired
            return false;
        }
        return true;
    }
}
