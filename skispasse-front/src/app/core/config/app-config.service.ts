import {Injectable} from '@angular/core';
import {AppConfig} from '../../shared/model/app-config.model';

@Injectable({providedIn: 'root'})
export class AppConfigService {

  readonly CONFIG_OPTIONS: AppConfig;

  constructor(private appConfig: AppConfig) {
    this.CONFIG_OPTIONS = appConfig;
  }

  getConfig(): AppConfig {
    return this.CONFIG_OPTIONS;
  }
}
