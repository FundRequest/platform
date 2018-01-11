import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {HttpClient} from '@angular/common/http';
import {RequestsStats} from '../../core/requests/RequestsStats';
import {IState} from '../../redux/store';
import {
  ClaimRequestCommand, createRequest, FundRequestCommand, IRequestList, IRequestRecord,
  SignedClaim
} from '../../redux/requests.models';
import {AddRequest, EditRequest, RemoveRequest, ReplaceRequestList} from '../../redux/requests.reducer';
import {IUserRecord} from '../../redux/user.models';
import {ContractsService} from '../contracts/contracts.service';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/take';

@Injectable()
export class RequestService {
  private _requestInitialized: boolean = false;

  constructor(private store: Store<IState>,
              private http: HttpClient,
              private _cs: ContractsService) {
  }

  public get requests$(): Observable<IRequestList> {
    if (!this._requestInitialized) {
      this._requestInitialized = true;

      this.http.get(`/api/public/requests`).take(1).subscribe((requests: IRequestList) => {
        this.store.dispatch(new ReplaceRequestList(requests));
        this.store.select(state => state.requests).take(1).subscribe((requests: IRequestList) => {
          requests.map((request: IRequestRecord) => {
            this._cs.getRequestFundInfo(request).then(
              (fundInfo) => {
                this.updateRequestWithNewFundInfo(request, fundInfo);
              }
            );
          });
        });
      });
    }

    return this.store.select(state => state.requests);
  }

  public async getStatistics(): Promise<RequestsStats> {
    return await this.http.get('/api/public/requests/statistics')
      .toPromise() as RequestsStats;
    //.then(response => response as RequestsStats)
    //.catch(this.handleError);
  }

  public addRequest(issueLink: string, fundAmount: number): void {
    let matches = /^https:\/\/github\.com\/(.+)\/(.+)\/issues\/(\d+)$/.exec(issueLink);
    let url = 'https://api.github.com/repos/' + matches[1] + '/' + matches[2] + '/issues/' + matches[3];
    this.http.get(url).subscribe(data => {
      this._cs.fundRequest("GITHUB", data['id'], issueLink, fundAmount);
    });

  }

  public async fundRequest(command: FundRequestCommand): Promise<string> {
    //let balance = await this.contractService.getRequestBalance(request) as string;
    return this._cs.fundRequest(command.platform, command.platformId, command.link, command.amount);
    // only edit request when funding is processed
    //this.editRequestInStore(request, createRequest(newRequest));
  }

  public async claimRequest(command: ClaimRequestCommand): Promise<string> {
    // this._cs.claimRequest(command.platform, command.platformId, command.link, command.amount);
    let body = {
      platform: command.platform,
      platformId: command.platformId,
      address: '0x7031dA35E02917250e9115E25Db5797f93443bDa'
    };
    await this.http.post('/api/private/requests/' + command.id + '/claim', body).subscribe((signedClaim: SignedClaim) => {
      console.log(signedClaim);
      return this._cs.claimRequest(signedClaim);

    });

    return null;

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

    let newRequest: IRequestRecord = createRequest(JSON.parse(JSON.stringify(request)));
    newRequest = newRequest.set('watchers', newWatchers);
    this.editRequestInStore(request, newRequest);

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
    );
  }

  public removeRequestInStore(request: IRequestRecord) {
    this.store.dispatch(new RemoveRequest(request));
  }

  public addRequestInStore(newRequest: IRequestRecord) {
    this.store.dispatch(new AddRequest(newRequest));
    this.updateRequestBalance(newRequest);
  }

  private updateRequestBalance(request: IRequestRecord) {
    this._cs.getRequestFundInfo(request).then(
      (fundInfo) => {
        this.updateRequestWithNewFundInfo(request, fundInfo);

      }
    );
  }

  private updateRequestWithNewFundInfo(request: IRequestRecord, fundInfo) {
    if (request.set) {
      let newRequest =
        request.set('balance', fundInfo.balance)
          .set('funderBalance', fundInfo.funderBalance)
          .set('numberOfFunders', fundInfo.numberOfFunders);
      this.editRequestInStore(request, newRequest);
    } else {
      let newRequest = request;
      newRequest.balance = fundInfo.balance;
      newRequest.funderBalance = fundInfo.funderBalance;
      newRequest.numberOfFunders = fundInfo.numberOfFunders;
      this.editRequestInStore(request, newRequest);
    }
  }

  public editRequestInStore(oldRequest: IRequestRecord, modifiedRequest: IRequestRecord) {
    this.store.dispatch(new EditRequest(oldRequest, modifiedRequest));
  }

  public editOrAddRequestInStore(newOrModifiedRequest: IRequestRecord) {
    let existingRequest: IRequestRecord;

    this.store.select(state => state.requests).take(1).subscribe((requests: IRequestList) => {
      requests.filter((request: IRequestRecord) => request.id == newOrModifiedRequest.id)
        .map((request: IRequestRecord) => {
          existingRequest = request;
        });
    });

    if (existingRequest == null) {
      this.addRequestInStore(newOrModifiedRequest);
    } else {
      this.updateRequestBalance(existingRequest);
    }
  }

  private handleError(error: any): void {
    console.error('An error occurred', error);
  }
}
