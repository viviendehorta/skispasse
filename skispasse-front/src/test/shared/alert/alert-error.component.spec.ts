import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {AlertErrorComponent} from '../../../app/shared/alert/alert-error.component';
import {EventManager} from '../../../app/core/events/event-manager';
import {AlertService} from '../../../app/core/alert/alert.service';
import {MockAlertService} from '../../mock/mock-alert.service';
import {SkispasseTestModule} from '../../test.module';
import {HttpErrorResponse, HttpHeaders} from '@angular/common/http';

describe('Component Tests', () => {
  describe('Alert Error Component', () => {
    let component: AlertErrorComponent;
    let fixture: ComponentFixture<AlertErrorComponent>;
    let eventManager: EventManager;

    beforeEach(async(() => {
      TestBed
        .configureTestingModule({
          declarations: [AlertErrorComponent],
          imports: [SkispasseTestModule],
          providers: [
            EventManager,
            {
              provide: AlertService,
              useClass: MockAlertService
            }
          ]
        })
        .overrideTemplate(AlertErrorComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      fixture = TestBed.createComponent(AlertErrorComponent);
      component = fixture.componentInstance;
      eventManager = fixture.debugElement.injector.get(EventManager);
    });

    describe('Error Handling', () => {
      it('Should display an alert on status 0', () => {
        // GIVEN
        eventManager.broadcast({name: 'skispasseApp.httpError', content: {status: 0}});
        // THEN
        expect(component.alerts.length).toBe(1);
        expect(component.alerts[0].msg).toBe('Server not reachable :(');
      });

      it('Should display an alert on status 404 without error object', () => {
        // GIVEN
        eventManager.broadcast({name: 'skispasseApp.httpError', content: {status: 404}});
        // THEN
        expect(component.alerts.length).toBe(1);
        expect(component.alerts[0].msg).toBe('Not found :O');
      });

      it('Should display an alert on status 404 with empty error', () => {
        // GIVEN
        eventManager.broadcast({name: 'skispasseApp.httpError', content: {status: 404, error: ''}});
        // THEN
        expect(component.alerts.length).toBe(1);
        expect(component.alerts[0].msg).toBe('Not found :O');
      });

      it('Should display an alert on status 404 with empty error.detail message', () => {
        // GIVEN
        eventManager.broadcast({name: 'skispasseApp.httpError', content: {status: 404, error: {detail: ''}}});
        // THEN
        expect(component.alerts.length).toBe(1);
        expect(component.alerts[0].msg).toBe('Not found :O');
      });

      it('Should display an alert on generic error', () => {
        // GIVEN
        eventManager.broadcast({ name: 'skispasseApp.httpError', content: { error: { detail: 'Error Message' } } });
        eventManager.broadcast({ name: 'skispasseApp.httpError', content: { error: 'Second Error Message' } });
        // THEN
        expect(component.alerts.length).toBe(2);
        expect(component.alerts[0].msg).toBe('Error Message');
        expect(component.alerts[1].msg).toBe('Second Error Message');
      });

      it('Should display an alert on status 504 for gateway timeout', () => {
        // GIVEN
        const response = new HttpErrorResponse({
          url: 'http://localhost:8080/api/foos',
          headers: new HttpHeaders(),
          status: 504,
          error: 'error.http.504'
        });
        eventManager.broadcast({ name: 'skispasseApp.httpError', content: response });
        // THEN
        expect(component.alerts.length).toBe(1);
        expect(component.alerts[0].msg).toBe('error.http.504');
      });
    });
  });
});
