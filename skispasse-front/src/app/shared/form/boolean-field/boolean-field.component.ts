import {Component, Input, OnInit} from '@angular/core';
import {AppConfig} from '../../model/app-config.model';
import {AppConfigService} from '../../../core/config/app-config.service';

@Component({
  selector: 'skis-boolean-field',
  template: `
    <span [ngClass]=\"value ? classTrue : classFalse\" [innerHtml]=\"value ? textTrue : textFalse\"> </span>
  `
})
export class BooleanFieldComponent implements OnInit {

  /**
   * the boolean input value
   */
  @Input() value: boolean;
  /**
   * the class(es) (space separated) that will be applied if value is true
   */
  @Input() classTrue: string;
  /**
   * the class(es) (space separated) that will be applied if the input value is false
   */
  @Input() classFalse: string;
  /**
   * the text that will be displayed if the input value is true
   */
  @Input() textTrue: string;
  /**
   * the text that will be displayed if the input value is false
   */
  @Input() textFalse: string;

  private config: AppConfig;

  constructor(private configService: AppConfigService) {
    this.config = configService.getConfig();
  }

  ngOnInit(): void {

    if (this.textTrue === undefined) {
      if (this.classTrue === undefined) {
        this.classTrue = this.config.classTrue;
      }
    } else {
      if (this.classTrue === undefined) {
        this.classTrue = this.config.classBadgeTrue;
      }
    }
    if (this.textFalse === undefined) {
      if (this.classFalse === undefined) {
        this.classFalse = this.config.classFalse;
      }
    } else {
      if (this.classFalse === undefined) {
        this.classFalse = this.config.classBadgeFalse;
      }
    }
  }
}
