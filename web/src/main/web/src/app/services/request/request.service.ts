import {Injectable} from '@angular/core';
import {AddRequest, EditRequest, IState, RemoveRequest, ReplaceRequestList} from "../../redux/store";
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';

import {TypedRecord, makeTypedFactory} from 'typed-immutable-record';
import {List} from 'immutable';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {User} from "../../core/user/User";
import {RequestsStats} from "../../core/requests/RequestsStats";

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
    this.http.get(`/api/private/requests`)
      .take(1).subscribe((response: IRequestList) => {
      this.store.dispatch(new ReplaceRequestList(response));
    }, error => this.handleError(error));

    return this.store.select(state => state.requests);
  }

  public getStatistics(): Promise<RequestsStats> {
    return this.http.get('/api/public/requests/statistics')
      .toPromise()
      .then(response => response as RequestsStats)
      .catch(this.handleError);
  }

  public addRequest(issueLink: string, technologies: String[]): void {
    this.http.post(`/api/private/requests/`, {
      issueLink: issueLink,
      technologies: technologies
    }, {observe: 'response'}).take(1).subscribe((result) => {

        let location = result.headers.get('location');
        if (location.length > 0) {
          this.http.get(location)
            .take(1).subscribe((request: IRequestRecord) => {
              this.addRequestInStore(createRequest(request));
            }, error => this.handleError(error)
          )
        }

      }, error => this.handleError(error)
    )
  }

  public setUserAsWatcher(request: IRequestRecord, user: User): void {
    this.updateWatcher(request, user.email, true);
  }

  public unSetUserAsWatcher(request: IRequestRecord, user: User): void {
    this.updateWatcher(request, user.email, false);
  }

  private updateWatcher(request: IRequestRecord, userId: string, add: boolean): void {
    let newWatchers: string[] = request.watchers.map(x => Object.assign({}, x));
    if (add) {
      newWatchers.push(userId);
    } else {
      newWatchers = newWatchers.filter(watcher => watcher != userId);
    }
    let newRequest: IRequestRecord = request.set('watchers', newWatchers);
    this.editRequestInStore(request, newRequest);
    this.http.put(`/api/private/requests/${request.id}/watchers`, {
      responseType: 'text',
      requestId: request.id
    }).take(1).subscribe(null,
      error => {
        this.editRequestInStore(newRequest, request); // if something when wrong, update it back to the old value
        this.handleError(error);
      }
    )
  }

  private removeRequestInStore(request: IRequestRecord) {
    this.store.dispatch(new RemoveRequest(request));
  }

  private addRequestInStore(newRequest: IRequestRecord) {
    this.store.dispatch(new AddRequest(newRequest));
  }

  private editRequestInStore(oldRequest: IRequestRecord, modifiedRequest: IRequestRecord) {
    this.store.dispatch(new EditRequest(oldRequest, modifiedRequest));
  }

  private handleError(error: any): void {
    console.error('An error occurred', error);
  }
}
