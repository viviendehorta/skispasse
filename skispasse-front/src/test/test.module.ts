import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { NgModule } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import {MockActivatedRoute, MockRouter} from './mock/mock-route.service';
import {MockActiveModal} from './mock/mock-active-modal.service';
import {MockEventManager} from './mock/mock-event-manager.service';
import {EventManager} from '../app/core/events/event-manager';
import {AccountService} from '../app/core/auth/account.service';
import {LoginModalService} from '../app/core/login/login-modal.service';
import {AlertService} from '../app/core/alert/alert.service';
import {MockAccountService} from './mock/mock-account.service';


@NgModule({
  providers: [
  DatePipe,
  {
    provide: EventManager,
    useClass: MockEventManager
  },
  {
    provide: NgbActiveModal,
    useClass: MockActiveModal
  },
  {
    provide: ActivatedRoute,
    useValue: new MockActivatedRoute({ id: '123' })
  },
  {
    provide: Router,
    useClass: MockRouter
  },
  {
    provide: AccountService,
    useClass: MockAccountService
  },
  {
    provide: LoginModalService,
    useValue: null
  },
  {
    provide: AlertService,
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
