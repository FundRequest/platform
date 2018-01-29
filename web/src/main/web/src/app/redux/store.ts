import {requestReducer} from './requests.reducer';
import {IRequestList} from './requests.models';
import {userReducer} from './user.reducer';
import {IUserRecord} from './user.models';
import {INotificationList} from './notifications.models';
import {notificationReducer} from './notifications.reducer';

export interface IState {
  requests: IRequestList;
  user: IUserRecord;
  notifications: INotificationList;
}

export const REDUCER_MAP = {
  requests: requestReducer,
  user: userReducer,
  notifications: notificationReducer
};
