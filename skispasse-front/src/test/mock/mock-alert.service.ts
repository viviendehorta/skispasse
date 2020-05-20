import {AlertService} from '../../app/core/alert/alert.service';
import {Alert, AlertType} from '../../app/shared/model/alert/alert.model';
import {SpyObject} from './spyobject';

export class MockAlertService extends SpyObject {

  constructor() {
    super(AlertService);
  }

  addAlert(type: AlertType, message: string, params: any, extAlerts: Alert[]): Alert {
    return new Alert(type, message, params);
  }
}
