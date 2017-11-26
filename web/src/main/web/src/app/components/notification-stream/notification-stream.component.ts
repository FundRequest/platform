import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { Store } from '@ngrx/store';
import { INotificationList, INotificationRecord } from '../../redux/notifications.models';
import { IState } from '../../redux/store';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector   : 'fnd-notification-stream',
  templateUrl: './notification-stream.component.html',
  styleUrls  : ['./notification-stream.component.scss']
})
export class NotificationStreamComponent implements OnInit, OnDestroy {
  private _subscription: Subscription;
  private _notifications: Array<INotificationRecord>;

  constructor(private _store: Store<IState>) {
  }

  public get notifications$(): Observable<Array<INotificationRecord>> {
    return Observable.of(this._notifications);
  }

  ngOnInit() {
    this._subscription = this._store.select(state => state.notifications).subscribe((list: INotificationList) => {
      this._notifications = list.toArray().sort((a: INotificationRecord, b: INotificationRecord) => {
        return a.date > b.date ? -1 : 1;
      });
    });
  }

  ngOnDestroy() {
    this._subscription.unsubscribe();
  }
}
