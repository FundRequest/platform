import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {HttpClient} from "@angular/common/http";
import {RequestsStats} from "../../core/requests/RequestsStats";
import {IState} from "../../redux/store";
import {createRequest, IRequestList, IRequestRecord} from "../../redux/requests.models";
import {AddRequest, EditRequest, RemoveRequest, ReplaceRequestList} from "../../redux/requests.reducer";
import {IUserRecord} from "../../redux/user.models";
import {ContractsService} from "../contracts/contracts.service";

import 'rxjs/add/operator/toPromise';

@Injectable()
export class RequestService {
  private _requestInitialized: boolean = false;

  constructor(private store: Store<IState>,
              private http: HttpClient,
              private contractService: ContractsService) {
  }

  public get requests$(): Observable<IRequestList> {
    if (!this._requestInitialized) {
      this._requestInitialized = true;

      this.http.get(`/api/private/requests`).take(1).subscribe((requests: IRequestList) => {
        this.store.dispatch(new ReplaceRequestList(requests));
      });

      this.store.select(state => state.requests).take(1).subscribe((requests: IRequestList) => {
        requests.forEach((request: IRequestRecord, index) => {
          this.contractService.getRequestBalance(request).then(
            balance => {
              this.editRequestInStore(request, createRequest(request.set('balance', balance)));
            }
          );
        })
      });
    }

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

  public async fundRequest(request: IRequestRecord, funding: number): Promise<string> {
    //let balance = await this.contractService.getRequestBalance(request) as string;
    return this.contractService.fundRequest(request, funding);
    // only edit request when funding is processed
    //this.editRequestInStore(request, createRequest(newRequest));
  }

  public setUserAsWatcher(request: IRequestRecord, user: IUserRecord): void {
    this.updateWatcher(request, user.email, true);
  }

  public unSetUserAsWatcher(request: IRequestRecord, user: IUserRecord): void {
    this.updateWatcher(request, user.email, false);
  }

  private updateWatcher(request: IRequestRecord, userId: string, add: boolean): void {
    let newWatchers: string[] = JSON.parse(JSON.stringify(request.watchers));

    if (add) {
      newWatchers.push(userId);
    } else {
      newWatchers = newWatchers.filter(function (watcher) {
        return watcher != userId;
      });
    }

    let newRequest = JSON.parse(JSON.stringify(request));
    newRequest.watchers = newWatchers;
    this.editRequestInStore(request, createRequest(newRequest));

    let httpUrl = `/api/private/requests/${request.id}/watchers`;
    let httpCall: Observable<Object>;

    if (add) {
      httpCall = this.http.put(httpUrl, {
        responseType: 'text',
        requestId: request.id
      });
    } else {
      httpCall = this.http.delete(httpUrl);
    }

    httpCall.take(1).subscribe(null,
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
