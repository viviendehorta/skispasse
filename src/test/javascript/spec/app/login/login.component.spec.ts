import { async, ComponentFixture, fakeAsync, inject, TestBed, tick } from '@angular/core/testing';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginService } from 'app/core/login/login.service';
import { StateStorageService } from 'app/core/auth/state-storage.service';
import { SkispasseTestModule } from '../../test.module';
import { MockLoginService } from '../../helpers/mock-login.service';
import { MockStateStorageService } from '../../helpers/mock-state-storage.service';
import { LoginModalComponent } from 'app/login/login.component';

describe('Component Tests', () => {
  describe('LoginComponent', () => {
    let comp: LoginModalComponent;
    let fixture: ComponentFixture<LoginModalComponent>;
    let mockLoginService: any;
    let mockStateStorageService: any;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [SkispasseTestModule],
        declarations: [LoginModalComponent],
        providers: [
          FormBuilder,
          {
            provide: LoginService,
            useClass: MockLoginService
          },
          {
            provide: StateStorageService,
            useClass: MockStateStorageService
          }
        ]
      })
        .overrideTemplate(LoginModalComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(LoginModalComponent);
      comp = fixture.componentInstance;
      mockLoginService = fixture.debugElement.injector.get(LoginService);
      mockStateStorageService = fixture.debugElement.injector.get(StateStorageService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    it('should authenticate the user upon login when previous state was set', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        const credentials = {
          username: 'admin',
          password: 'admin',
          rememberMe: true
        };

        comp.loginForm.patchValue({
          username: 'admin',
          password: 'admin',
          rememberMe: true
        });
        mockLoginService.setResponse({});

        // WHEN/
        comp.login();
        tick(); // simulate async

        // THEN
        expect(comp.authenticationError).toEqual(false);
        expect(mockActiveModal.dismissSpy).toHaveBeenCalledWith('login success');
        expect(mockEventManager.broadcastSpy).toHaveBeenCalledTimes(1);
        expect(mockLoginService.loginSpy).toHaveBeenCalledWith(credentials);
        expect(mockStateStorageService.getUrlSpy).toHaveBeenCalledTimes(1);
        expect(mockStateStorageService.storeUrlSpy).toHaveBeenCalledWith(null);
      })
    ));

    it('should authenticate the user upon login when previous state was not set', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        const credentials = {
          username: 'admin',
          password: 'admin',
          rememberMe: true
        };
        comp.loginForm.patchValue({
          username: 'admin',
          password: 'admin',
          rememberMe: true
        });
        mockLoginService.setResponse({});
        mockStateStorageService.setResponse(null);

        // WHEN
        comp.login();
        tick(); // simulate async

        // THEN
        expect(comp.authenticationError).toEqual(false);
        expect(mockActiveModal.dismissSpy).toHaveBeenCalledWith('login success');
        expect(mockEventManager.broadcastSpy).toHaveBeenCalledTimes(1);
        expect(mockLoginService.loginSpy).toHaveBeenCalledWith(credentials);
        expect(mockStateStorageService.getUrlSpy).toHaveBeenCalledTimes(1);
        expect(mockStateStorageService.storeUrlSpy).not.toHaveBeenCalled();
      })
    ));

    it('should empty the credentials upon cancel', () => {
      // GIVEN

      comp.loginForm.patchValue({
        username: 'admin',
        password: 'admin'
      });

      const expected = {
        username: '',
        password: '',
        rememberMe: false
      };

      // WHEN
      comp.cancel();

      // THEN
      expect(comp.authenticationError).toEqual(false);
      expect(comp.loginForm.get('username').value).toEqual(expected.username);
      expect(comp.loginForm.get('password').value).toEqual(expected.password);
      expect(comp.loginForm.get('rememberMe').value).toEqual(expected.rememberMe);
      expect(mockActiveModal.dismissSpy).toHaveBeenCalledWith('cancel');
    });
  });
});
