import {IconDefinition} from '@fortawesome/free-solid-svg-icons';

export class AppConfig {
    sortIcon?: IconDefinition;
    sortAscIcon?: IconDefinition;
    sortDescIcon?: IconDefinition;
    alertTimeout?: number;
    classBadgeTrue?: string;
    classBadgeFalse?: string;
    classTrue?: string;
    classFalse?: string;


  constructor(sortIcon: IconDefinition, sortAscIcon: IconDefinition, sortDescIcon: IconDefinition, alertTimeout: number,
              classBadgeTrue: string, classBadgeFalse: string, classTrue: string, classFalse: string) {
    this.sortIcon = sortIcon;
    this.sortAscIcon = sortAscIcon;
    this.sortDescIcon = sortDescIcon;
    this.alertTimeout = alertTimeout;
    this.classBadgeTrue = classBadgeTrue;
    this.classBadgeFalse = classBadgeFalse;
    this.classTrue = classTrue;
    this.classFalse = classFalse;
  }
}
