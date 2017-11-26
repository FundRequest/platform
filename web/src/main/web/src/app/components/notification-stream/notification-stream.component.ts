import { Component, OnInit } from '@angular/core';
import { NotificationStreamService } from '../../services/sse/notification-stream.service';
import { NotificationStreamMessage } from '../../services/sse/notification-stream-message';
import { Observable } from 'rxjs/Observable';
import { Store } from '@ngrx/store';
import { INotificationList } from '../../redux/notifications.models';
import { IState } from '../../redux/store';

@Component({
  selector   : 'fnd-notification-stream',
  templateUrl: './notification-stream.component.html',
  styleUrls  : ['./notification-stream.component.scss']
})
export class NotificationStreamComponent implements OnInit {

  constructor(private _store: Store<IState>) {
  }

  public get notifications$(): Observable<INotificationList> {
    return this._store.select(state => state.notifications);
  }

  ngOnInit() {
  }

}
