import { Action } from '@ngrx/store';
import {createAccountWeb3, IAccountWeb3Record} from './accountWeb3.models';

export abstract class AccountWeb3Action implements Action {
  constructor(public type: string) {
  }

  public abstract handle(state: IAccountWeb3Record): IAccountWeb3Record;
}

export class ReplaceAccountWeb3 extends AccountWeb3Action {
  constructor(private modifiedItem: IAccountWeb3Record) {
    super('REPLACE_ACCOUNT_WEB3');
  }

  public handle(state: IAccountWeb3Record): IAccountWeb3Record {
    return state = this.modifiedItem;
  }
}

export type ActionTypes = ReplaceAccountWeb3;

const INITIAL_ACCOUNT_WEB3 = createAccountWeb3();

export function accountWeb3Reducer(state: IAccountWeb3Record = INITIAL_ACCOUNT_WEB3, action: ActionTypes): IAccountWeb3Record {
  if (action instanceof AccountWeb3Action) return action.handle(state);
  else return state;
}
