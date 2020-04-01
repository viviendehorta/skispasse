import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NgModule } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService, JhiDataUtils, JhiDateUtils, JhiEventManager, JhiLanguageService, JhiParseLinks } from 'ng-jhipster';

import { MockLanguageHelper, MockLanguageService } from './helpers/mock-language.service';
import { JhiLanguageHelper } from 'app/core/language/language.helper';
import { MockActivatedRoute, MockRouter } from './helpers/mock-route.service';
import { MockEventManager } from './helpers/mock-event-manager.service';

@NgModule({
  providers: [
    DatePipe,
    JhiDataUtils,
    JhiDateUtils,
    JhiParseLinks,
    {
      provide: JhiLanguageService,
      useClass: MockLanguageService
    },
    {
      provide: JhiLanguageHelper,
      useClass: MockLanguageHelper
    },
    {
      provide: JhiEventManager,
      useClass: MockEventManager
    },
    {
      provide: ActivatedRoute,
      useValue: new MockActivatedRoute({ id: 123 })
    },
    {
      provide: Router,
      useClass: MockRouter
    },
    {
      provide: JhiAlertService,
      useValue: null
    },
    {
      provide: NgbModal,
      useValue: null
    }
  ],
  imports: [HttpClientTestingModule]
})
export class SkispasseTestModule {}
