import { Injectable } from '@angular/core';
import { EventSourcePolyfill, OnMessageEvent } from 'ng-event-source';
import { environment } from '../../../environments/environment';
import { RequestService } from '../request/request.service';
import { NotificationStreamMessage } from './notification-stream-message';
import { Observable } from 'rxjs/Observable';
import { Store } from '@ngrx/store';
import { AddNotification } from '../../redux/notifications.reducer';
import { createNotification, INotificationRecord } from '../../redux/notifications.models';
import { IState } from '../../redux/store';
import { IRequestRecord } from '../../redux/requests.models';
import { AddRequest } from '../../redux/requests.reducer';

@Injectable()
export class NotificationStreamService {

  private _eventSource: EventSourcePolyfill;

  constructor(private _rs: RequestService, private _store: Store<IState>) {
    this._eventSource = new EventSourcePolyfill(`${environment.restApiLocation}/api/public/notifications-stream`, {});
    this._eventSource.onmessage = ((messageEvent: OnMessageEvent) => {
      let notificationStreamMessage = new NotificationStreamMessage(JSON.parse(messageEvent.data));
      this._commit(notificationStreamMessage);
    });
    this._eventSource.onopen = (a) => {
      console.log('open', a);// Do stuff here
    };
    this._eventSource.onerror = (e) => {
      // Do stuff here
      console.log('error', e);
    };
  }

  private _commit(message: NotificationStreamMessage) {
    this._addNotificationInStore(message.notification);
    this._addRequestInStore(message.request);
  }

  private _addNotificationInStore(newNotification: INotificationRecord) {
    this._store.dispatch(new AddNotification(newNotification));
  }

  private _addRequestInStore(newRequest: IRequestRecord) {
    this._store.dispatch(new AddRequest(newRequest));
  }
}

