import { Injectable, NgZone } from '@angular/core';
import { EventSourcePolyfill, OnMessageEvent } from 'ng-event-source';
import { environment } from '../../../environments/environment';

@Injectable()
export class NotificationStreamService {

  private _eventSource: EventSourcePolyfill;

  constructor(private zone: NgZone) {
    this._eventSource = new EventSourcePolyfill(`${environment.restApiLocation}/api/public/notifications-stream`, {});
    this._eventSource.onmessage = ((messageEvent: OnMessageEvent) => {
        console.log(JSON.parse(messageEvent.data));
    });
    this._eventSource.onopen = (a) => {
      console.log('open', a);// Do stuff here
    };
    this._eventSource.onerror = (e) => {
      // Do stuff here
      console.log('error', e);
    };
  }
}
