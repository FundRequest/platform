import {requestReducer} from './requests.reducer';
import {IRequestList} from './requests.models';
import {userReducer} from './user.reducer';
import {IUserRecord} from './user.models';
import {INotificationList} from './notifications.models';
import {notificationReducer} from './notifications.reducer';
import {accountWeb3Reducer} from './accountWeb3.reducer';
import {IAccountWeb3Record} from './accountWeb3.models';

export interface IState {
  requests: IRequestList;
  user: IUserRecord;
  notifications: INotificationList;
  accountWeb3: IAccountWeb3Record;
}

export const REDUCER_MAP = {
  requests: requestReducer,
  user: userReducer,
  notifications: notificationReducer,
  accountWeb3: accountWeb3Reducer,
};
