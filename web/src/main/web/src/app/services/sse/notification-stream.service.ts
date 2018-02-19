import {Injectable} from '@angular/core';
import {EventSourcePolyfill, OnMessageEvent} from 'ng-event-source';
import {environment} from '../../../environments/environment';
import {RequestService} from '../request/request.service';
import {NotificationStreamMessage} from './notification-stream-message';
import {Store} from '@ngrx/store';
import {AddNotification} from '../../redux/notifications.reducer';
import {createNotification, INotificationRecord} from '../../redux/notifications.models';
import {IState} from '../../redux/store';
import {createRequest} from '../../redux/requests.models';
import {ApiUrls} from '../../api/api.urls';

@Injectable()
export class NotificationStreamService {

  private _eventSource: EventSourcePolyfill;

  constructor(private _rs: RequestService, private _store: Store<IState>) {
    this._eventSource = new EventSourcePolyfill(`${environment.restApiLocation}${ApiUrls.notificationStream}`, {heartbeatTimeout: 18000000});
    this._eventSource.onmessage = ((messageEvent: OnMessageEvent) => {
      let notificationStreamMessage = new NotificationStreamMessage(JSON.parse(messageEvent.data));
      this._commit(notificationStreamMessage);
    });
    this._eventSource.onopen = (a) => {
    };
    this._eventSource.onerror = (e) => {
    };
  }

  private _commit(message: NotificationStreamMessage) {
    this.addNotificationInStore(createNotification(message.notification));
    this._rs.editOrAddRequestInStore(createRequest(message.request));
  }

  public addNotificationInStore(newNotification: INotificationRecord) {
    this._store.dispatch(new AddNotification(newNotification));
  }
}
