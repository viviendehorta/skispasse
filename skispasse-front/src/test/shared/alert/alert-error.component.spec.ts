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
        expect(component.alerts[0].msg).toBe('Server not reachable');
      });
      it('Should display an alert on status 404', () => {
        // GIVEN
        eventManager.broadcast({name: 'skispasseApp.httpError', content: {status: 404}});
        // THEN
        expect(component.alerts.length).toBe(1);
        expect(component.alerts[0].msg).toBe('Not found');
      });
      it('Should display an alert on generic error', () => {
        // GIVEN
        eventManager.broadcast({ name: 'skispasseApp.httpError', content: { error: { message: 'Error Message' } } });
        eventManager.broadcast({ name: 'skispasseApp.httpError', content: { error: 'Second Error Message' } });
        // THEN
        expect(component.alerts.length).toBe(2);
        expect(component.alerts[0].msg).toBe('Error Message');
        expect(component.alerts[1].msg).toBe('Second Error Message');
      });
      it('Should display an alert on status 400 for generic error', () => {
        // GIVEN
        const response = new HttpErrorResponse({
          url: 'http://localhost:8080/api/foos',
          headers: new HttpHeaders(),
          status: 400,
          statusText: 'Bad Request',
          error: {
            type: 'https://www.jhipster.tech/problem/constraint-violation',
            title: 'Bad Request',
            status: 400,
            path: '/api/foos',
            message: 'error.validation'
          }
        });
        eventManager.broadcast({ name: 'skispasseApp.httpError', content: response });
        // THEN
        expect(component.alerts.length).toBe(1);
        expect(component.alerts[0].msg).toBe('error.validation');
      });
      it('Should display an alert on status 400 for generic error without message', () => {
        // GIVEN
        const response = new HttpErrorResponse({
          url: 'http://localhost:8080/api/foos',
          headers: new HttpHeaders(),
          status: 400,
          error: 'Bad Request'
        });
        eventManager.broadcast({ name: 'skispasseApp.httpError', content: response });
        // THEN
        expect(component.alerts.length).toBe(1);
        expect(component.alerts[0].msg).toBe('Bad Request');
      });
      it('Should display an alert on status 400 for invalid parameters', () => {
        // GIVEN
        const response = new HttpErrorResponse({
          url: 'http://localhost:8080/api/foos',
          headers: new HttpHeaders(),
          status: 400,
          statusText: 'Bad Request',
          error: {
            type: 'https://www.jhipster.tech/problem/constraint-violation',
            title: 'Method argument not valid',
            status: 400,
            path: '/api/foos',
            message: 'error.validation',
            fieldErrors: [{ objectName: 'foo', field: 'minField', message: 'Min' }]
          }
        });
        eventManager.broadcast({ name: 'skispasseApp.httpError', content: response });
        // THEN
        expect(component.alerts.length).toBe(1);
        expect(component.alerts[0].msg).toBe('Error on field "MinField"');
      });
      it('Should display an alert on status 400 for error headers', () => {
        // GIVEN
        const response = new HttpErrorResponse({
          url: 'http://localhost:8080/api/foos',
          headers: new HttpHeaders().append('app-error', 'Error Message').append('app-params', 'foo'),
          status: 400,
          statusText: 'Bad Request',
          error: {
            status: 400,
            message: 'error.validation'
          }
        });
        eventManager.broadcast({ name: 'skispasseApp.httpError', content: response });
        // THEN
        expect(component.alerts.length).toBe(1);
        expect(component.alerts[0].msg).toBe('Error Message');
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
