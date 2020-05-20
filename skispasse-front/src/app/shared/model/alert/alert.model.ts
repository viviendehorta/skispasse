export type AlertType = 'success' | 'danger' | 'warning' | 'info';

export class Alert {
  type: AlertType;
  msg: string;
  params: string;
  alertId: number;
  close: (alerts: Alert[]) => void;


  constructor(type: AlertType, msg: string, params: string, alertId?: number, close?: (alerts: Alert[]) => void) {
    this.type = type;
    this.msg = msg;
    this.params = params;
    this.alertId = alertId ? alertId : null;
    this.close = close ? close : (alerts: []) => alerts; // By default, do nothing and returns input alerts
  }
}
