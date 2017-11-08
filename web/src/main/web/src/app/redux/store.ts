import {requestReducer} from "./requests.reducer";
import {IRequestList} from "./requests.models";
import {userReducer} from "./user.reducer";
import {IUserRecord} from "./user.models";

export interface IState {
  requests: IRequestList;
  user: IUserRecord;
}

export const REDUCER_MAP = {
  requests: requestReducer,
  user: userReducer
};
