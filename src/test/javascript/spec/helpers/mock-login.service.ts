import { of } from 'rxjs';
import { SpyObject } from './spyobject';
import { LoginService } from 'app/core/login/login.service';
import Spy = jasmine.Spy;

export class MockLoginService extends SpyObject {
  loginSpy: Spy;
  logoutSpy: Spy;
  cancelSpy: Spy;

  constructor() {
    super(LoginService);

    this.setLoginSpy({});
    this.logoutSpy = this.spy('logout').andReturn(this);
    this.cancelSpy = this.spy('cancel').andReturn(this);
  }

  setLoginSpy(json: any) {
    this.loginSpy = this.spy('login').andReturn(of(json));
  }

  setResponse(json: any): void {
    this.setLoginSpy(json);
  }
}
