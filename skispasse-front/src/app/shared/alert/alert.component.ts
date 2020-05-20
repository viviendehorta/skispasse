import { Component, OnDestroy, OnInit } from '@angular/core';
import {AlertService} from '../../core/alert/alert.service';
import {Alert} from '../model/alert/alert.model';

@Component({
  selector: 'skis-alert',
  template: `
    <div class="alerts" role="alert">
      <div *ngFor="let alert of alerts" [ngClass]="setClasses(alert)">
        <ngb-alert *ngIf="alert && alert.type && alert.msg" [type]="alert.type" (close)="alert.close(alerts)">
          <pre [innerHTML]="alert.msg"></pre>
        </ngb-alert>
      </div>
    </div>
  `
})
export class AlertComponent implements OnInit, OnDestroy {
  alerts: Alert[];

  constructor(private alertService: AlertService) {}

  ngOnInit() {
    this.alerts = this.alertService.get();
  }

  setClasses(alert) {
    return {
      [alert.position]: true
    };
  }

  ngOnDestroy() {
    this.alerts = [];
  }
}
