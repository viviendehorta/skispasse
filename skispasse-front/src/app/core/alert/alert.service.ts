import {Injectable, Sanitizer, SecurityContext} from '@angular/core';
import {AppConfigService} from '../config/app-config.service';
import {Alert, AlertType} from '../../shared/model/alert/alert.model';

@Injectable({providedIn: 'root'})
export class AlertService {

  private alertId = 0; // unique id for each alert. Starts from 0.
  private alerts: Alert[];
  private timeout: number;

  constructor(
    private sanitizer: Sanitizer,
    private configService: AppConfigService,
  ) {
    this.sanitizer = sanitizer;
    this.configService = configService;
    const config = this.configService.getConfig();
    this.alertId = 0; // unique id for each alert. Starts from 0.
    this.alerts = [];
    this.timeout = config.alertTimeout;
  }

  clear(): void {
    this.alerts.splice(0, this.alerts.length);
  }

  get(): Alert[] {
    return this.alerts;
  }

  success(msg, params): Alert {
    return this.addAlert('success', msg, params, []);
  }

  error(msg, params): Alert {
    return this.addAlert('danger', msg, params, []);
  }

  warning(msg, params): Alert {
    return this.addAlert('warning', msg, params, []);
  }

  info(msg, params): Alert {
    return this.addAlert('info', msg, params, []);
  }

  addAlert(type: AlertType, message: string, params: any, extAlerts: Alert[]): Alert {
    const alert = this.buildAlert(type, message, params);
    this.alerts.push(alert);
    setTimeout(() => {
      this.closeAlert(alert.alertId, extAlerts);
    }, this.timeout);
    return alert;
  }

  closeAlert(alertId: number, extAlerts: Alert[]): Alert[] {
    const thisAlerts = extAlerts && extAlerts.length > 0 ? extAlerts : this.alerts;
    return this.closeAlertByIndex(thisAlerts.map(e => e.alertId).indexOf(alertId), thisAlerts);
  }

  closeAlertByIndex(index, thisAlerts): Alert[] {
    return thisAlerts.splice(index, 1);
  }

  buildAlert(type: AlertType, message: string, params: any): Alert {
    const nextAlertId = this.alertId++;
    const alert = new Alert(
      type,
      this.sanitizer.sanitize(SecurityContext.HTML, message),
      params,
      nextAlertId,
      (alerts) => {
        return this.closeAlert(alert.alertId, alerts);
      }
    );
    return alert;
  }
}
