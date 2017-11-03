import {Injectable} from '@angular/core';
import {AddRequest, EditRequest, IState} from "../../redux/store";
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';

import {TypedRecord, makeTypedFactory} from 'typed-immutable-record';
import {List} from 'immutable';
import {HttpClient} from "@angular/common/http";

interface IRequestSource {
  key: String;
  value: String;
}


interface IRequestIssueInformation {
  link: String;
  owner: String;
  repo: String;
  number: Number;
  title: String;

  source: IRequestSource;
}

interface IRequest {
  id: number;
  status: string;
  type: string;
  technologies: string[];
  watchers: string[];
  loggedInUserIsWatcher: boolean;
  balance: string;
  issueInformation: IRequestIssueInformation;
}


export interface IRequestRecord extends TypedRecord<IRequestRecord>, IRequest {
}

export const createRequest = makeTypedFactory<IRequest, IRequestRecord>({
  id: 0,
  status: '',
  type: '',
  technologies: [],
  watchers: [],
  loggedInUserIsWatcher: false,
  balance: '0',
  issueInformation: {
    link: '',
    owner: '',
    repo: '',
    number: 0,
    title: '',
    source: {
      key: '',
      value: ''
    }
  }
});

export type IRequestList = List<IRequestRecord>;

@Injectable()
export class RequestService {
  constructor(private store: Store<IState>, private http: HttpClient) {
  }

  public get requests(): Observable<IRequestList> {
    return this.store.select(state => state.requests);
  }

  public addRequest(issueLink: string, technologies: String[]) {
    this.http.post(`/api/private/requests/`, {
      issueLink: issueLink,
      technologies: technologies
    }).take(1).subscribe(
      (request: IRequestRecord) => {
        let newRequest = createRequest(request);
        console.log(newRequest);
        this.store.dispatch(new AddRequest(newRequest));
      },
      (error) => this.handleError(error)
    )
  }

  public changeRequest(oldRequest: IRequestRecord, modifiedRequest: IRequestRecord) {
    this.store.dispatch(new EditRequest(oldRequest, modifiedRequest));
  }

  private handleError(error: any): void {
    console.error('An error occurred', error); // for demo purposes only
  }
}
