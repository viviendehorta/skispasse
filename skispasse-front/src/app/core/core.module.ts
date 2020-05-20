import {LOCALE_ID, NgModule} from '@angular/core';
import {DatePipe, registerLocaleData} from '@angular/common';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {Title} from '@angular/platform-browser';
import {FaIconLibrary} from '@fortawesome/angular-fontawesome';
import {fas, faSort, faSortDown, faSortUp} from '@fortawesome/free-solid-svg-icons';
import {CookieModule} from 'ngx-cookie';
import {NgxWebstorageModule} from 'ngx-webstorage';
import locale from '@angular/common/locales/fr';
import {NgbDateAdapter, NgbDateNativeAdapter} from '@ng-bootstrap/ng-bootstrap';
import {fontAwesomeIcons} from './icons/font-awesome-icons';
import {NotificationInterceptor} from '../blocks/interceptor/notification.interceptor';
import {ErrorHandlerInterceptor} from '../blocks/interceptor/errorhandler.interceptor';
import {AppConfig} from '../shared/model/app-config.model';

@NgModule({
  imports: [
    HttpClientModule,
    CookieModule.forRoot(),
    NgxWebstorageModule.forRoot({prefix: 'skis', separator: '-'})
  ],
  providers: [
    Title,
    {
      provide: LOCALE_ID,
      useValue: 'fr'
    },
    {provide: NgbDateAdapter, useClass: NgbDateNativeAdapter},
    DatePipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ErrorHandlerInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: NotificationInterceptor,
      multi: true
    },
    {
      provide: AppConfig,
      useValue: new AppConfig(
        faSort,
        faSortUp,
        faSortDown,
        10000,
        'badge badge-success',
        'badge badge-danger',
        'fa fa-lg fa-check text-success',
        'fa fa-lg fa-times text-danger'
      )
    }
  ]
})
export class SkispasseCoreModule {
  constructor(
    iconLibrary: FaIconLibrary,
  ) {
    registerLocaleData(locale);
    iconLibrary.addIconPacks(fas);
    iconLibrary.addIcons(...fontAwesomeIcons);
  }
}
