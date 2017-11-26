import { List } from 'immutable';
import { Action } from '@ngrx/store';
import { INotificationList, INotificationRecord } from './notifications.models';

export abstract class NotificationAction implements Action {
  constructor(public type: string) {
  }

  public abstract handle(state: INotificationList): INotificationList;
}

export class AddNotification extends NotificationAction {
  constructor(private newItem: INotificationRecord) {
    super('ADD_NOTIFICATION');
  }

  public handle(state: INotificationList): INotificationList {
    if (state.indexOf(this.newItem) != -1) {
      return state;
    }
    return state.push(this.newItem);
  }
}

export type ActionTypes = AddNotification;

const INITIAL_LIST = List<INotificationRecord>();

export function notificationReducer(state: INotificationList = INITIAL_LIST, action: ActionTypes): INotificationList {
  if (action instanceof NotificationAction) return action.handle(state);
  else return state;
}
