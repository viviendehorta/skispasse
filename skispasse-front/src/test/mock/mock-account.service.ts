import Spy = jasmine.Spy;
import { of } from 'rxjs';

import { SpyObject } from './spyobject';
import {AccountService} from '../../app/core/auth/account.service';

export class MockAccountService extends SpyObject {
  getSpy: Spy;
  saveSpy: Spy;
  authenticateSpy: Spy;
  identitySpy: Spy;
  getAuthenticationStateSpy: Spy;

  constructor() {
    super(AccountService);

    this.getSpy = this.spy('get').andReturn(this);
    this.saveSpy = this.spy('update').andReturn(this);
    this.authenticateSpy = this.spy('authenticate').andReturn(this);
    this.identitySpy = this.spy('identity').andReturn(of(null));
    this.getAuthenticationStateSpy = this.spy('getAuthenticationState').andReturn(of(null));
  }

  setIdentityResponse(account: Account | null): void {
    this.identitySpy = this.spy('identity').andReturn(of(account));
    this.getAuthenticationStateSpy = this.spy('getAuthenticationState').andReturn(of(account));
  }
}
