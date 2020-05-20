import {Component, OnDestroy} from '@angular/core';
import {Subscription} from 'rxjs';
import {AlertService} from '../../core/alert/alert.service';
import {EventManager} from '../../core/events/event-manager';
import {Alert} from '../model/alert/alert.model';
import {HttpErrorResponse} from '@angular/common/http';
import {AlertError} from '../model/alert/alert-error.model';
import {EventWithContent} from '../model/event-with-content.model';

@Component({
  selector: 'skis-alert-error',
  templateUrl: './alert-error.component.html'
})
export class AlertErrorComponent implements OnDestroy {

  alerts: Alert[] = [];
  errorListener: Subscription;
  httpErrorListener: Subscription;

  constructor(private alertService: AlertService, private eventManager: EventManager) {
    this.errorListener = eventManager.subscribe('skispasseApp.error', (response: EventWithContent<AlertError>) => {
      const errorResponse = response.content;
      this.addErrorAlert(errorResponse.message);
    });

    this.httpErrorListener = eventManager.subscribe('skispasseApp.httpError', (response: EventWithContent<HttpErrorResponse>) => {
      const httpErrorResponse = response.content;
      switch (httpErrorResponse.status) {
        // connection refused, server not reachable
        case 0:
          this.addErrorAlert('Server not reachable');
          break;

        case 400: {
          const arr = httpErrorResponse.headers.keys();
          let errorHeader = null;
          arr.forEach(entry => {
            if (entry.toLowerCase().endsWith('app-error')) {
              errorHeader = httpErrorResponse.headers.get(entry);
            }
          });
          if (errorHeader) {
            this.addErrorAlert(errorHeader);
          } else if (httpErrorResponse.error !== '' && httpErrorResponse.error.fieldErrors) {
            const fieldErrors = httpErrorResponse.error.fieldErrors;
            for (const fieldError of fieldErrors) {
              if (['Min', 'Max', 'DecimalMin', 'DecimalMax'].includes(fieldError.message)) {
                fieldError.message = 'Size';
              }
              // convert 'something[14].other[4].id' to 'something[].other[].id' so translations can be written to it
              const convertedField = fieldError.field.replace(/\[\d*\]/g, '[]');
              const fieldName = convertedField.charAt(0).toUpperCase() + convertedField.slice(1);
              this.addErrorAlert('Error on field "' + fieldName + '"');
            }
          } else if (httpErrorResponse.error !== '' && httpErrorResponse.error.message) {
            this.addErrorAlert(httpErrorResponse.error.message);
          } else {
            this.addErrorAlert(httpErrorResponse.error);
          }
          break;
        }

        case 404:
          this.addErrorAlert('Not found');
          break;

        default:
          if (httpErrorResponse.error !== '' && httpErrorResponse.error.message) {
            this.addErrorAlert(httpErrorResponse.error.message);
          } else {
            this.addErrorAlert(httpErrorResponse.error);
          }
      }
    });
  }

  ngOnDestroy(): void {
    if (this.errorListener) {
      this.eventManager.destroy(this.errorListener);
    }
    if (this.httpErrorListener) {
      this.eventManager.destroy(this.httpErrorListener);
    }
  }

  addErrorAlert(message: string): void {
    const alerts = this.alertService.addAlert('danger', message, null, this.alerts);
    this.alerts.push(alerts);
  }
}
