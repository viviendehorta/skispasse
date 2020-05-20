import {SpyObject} from './spyobject';
import {EventManager} from '../../app/core/events/event-manager';
import Spy = jasmine.Spy;

export class MockEventManager extends SpyObject {
  broadcastSpy: Spy;

  constructor() {
    super(EventManager);
    this.broadcastSpy = this.spy('broadcast').andReturn(this);
  }
}
