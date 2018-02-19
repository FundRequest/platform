import {requestReducer} from './requests.reducer';
import {IRequestList} from './requests.models';
import {userReducer} from './user.reducer';
import {IUserRecord} from './user.models';
import {INotificationList} from './notifications.models';
import {notificationReducer} from './notifications.reducer';
import {accountWeb3Reducer} from './accountWeb3.reducer';
import {IAccountWeb3Record} from './accountWeb3.models';

export interface IState {
  accountWeb3: IAccountWeb3Record;
  notifications: INotificationList;
  requests: IRequestList;
  user: IUserRecord;
}

export const REDUCER_MAP = {
  accountWeb3: accountWeb3Reducer,
  notifications: notificationReducer,
  requests: requestReducer,
  user: userReducer
};
