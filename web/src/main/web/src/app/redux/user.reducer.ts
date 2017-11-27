import { Action } from '@ngrx/store';
import { createUser, IUserRecord } from './user.models';

export abstract class UserAction implements Action {
  constructor(public type: string) {
  }

  public abstract handle(state: IUserRecord): IUserRecord;
}

export class ClearUser extends UserAction {
  constructor() {
    super('CLEAR_USER');
  }

  public handle(state: IUserRecord): IUserRecord {
    return state.clear();
  }
}

export class ReplaceUser extends UserAction {
  constructor(private modifiedUser: IUserRecord) {
    super('REPLACE_USER');
  }

  public handle(state: IUserRecord): IUserRecord {
    return state = this.modifiedUser;
  }
}

export type ActionTypes = ClearUser | ReplaceUser;

const INITIAL_USER = createUser();

export function userReducer(state: IUserRecord = INITIAL_USER, action: ActionTypes): IUserRecord {
  if (action instanceof UserAction) return action.handle(state);
  else return state;
}
