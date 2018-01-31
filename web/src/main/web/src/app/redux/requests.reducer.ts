import { List } from 'immutable';
import { Action } from '@ngrx/store';
import { IRequestList, IRequestRecord } from './requests.models';

export abstract class RequestAction implements Action {
  constructor(public type: string) {
  }

  public abstract handle(state: IRequestList): IRequestList;
}

export class AddRequest extends RequestAction {
  constructor(private newItem: IRequestRecord) {
    super('ADD_REQUEST');
  }

  public handle(state: IRequestList): IRequestList {
    if (state.findIndex((item: IRequestRecord) => item.id == this.newItem.id) != -1) {
      return state;
    }
    return state.push(this.newItem);
  }
}

export class RemoveRequest extends RequestAction {
  constructor(private item: IRequestRecord) {
    super('REMOVE_REQUEST');
  }

  public handle(state: IRequestList): IRequestList {
    const index = state.indexOf(this.item);
    return state.remove(index);
  }
}

export class EditRequest extends RequestAction {
  constructor(private oldItem: IRequestRecord, private modifiedItem: IRequestRecord) {
    super('EDIT_REQUEST');
  }

  public handle(state: IRequestList): IRequestList {
    const index = state.indexOf(this.oldItem);
    return state.set(index, this.modifiedItem);
  }
}

export class ReplaceRequestList extends RequestAction {
  constructor(private requestList: Array<IRequestRecord>) {
    super('REPLACE_REQUEST_LIST');
  }

  public handle(state: IRequestList): IRequestList {
    return List<IRequestRecord>(this.requestList);
  }
}

export type ActionTypes = AddRequest | EditRequest | RemoveRequest | ReplaceRequestList;

const INITIAL_LIST = List<IRequestRecord>();

export function requestReducer(state: IRequestList = INITIAL_LIST, action: ActionTypes): IRequestList {
  if (action instanceof RequestAction) return action.handle(state);
  else return state;
}
