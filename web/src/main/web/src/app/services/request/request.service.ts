import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Store} from '@ngrx/store';
import {HttpClient} from '@angular/common/http';
import {RequestsStats} from '../../core/requests/RequestsStats';
import {IState} from '../../redux/store';
import {
  ClaimRequestCommand,
  createRequest,
  FundRequestCommand,
  IRequest,
  IRequestList,
  IRequestRecord,
  RequestIssueFundInformation,
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

      this.http.get(`/api/public/requests`).take(1).subscribe((requests: Array<IRequest>) => {
        let requestsList: Array<IRequestRecord> = [];
        requests.forEach((request) => {
          requestsList.push(createRequest(request));
        });
        this.store.dispatch(new ReplaceRequestList(requestsList));
        this.store.select(state => state.requests).take(1).subscribe((requests: IRequestList) => {
          requests.map((request: IRequestRecord) => {
            this.updateRequestWithNewFundInfoFromContract(request);
          });
        });
      });
    }

    return this.store.select(state => state.requests);
  }

  public async getStatistics(): Promise<RequestsStats> {
    return this._cs.getStatistics();
  }

  public addRequest(issueLink: string, fundAmount: number): void {
    let matches = /^https:\/\/github\.com\/(.+)\/(.+)\/issues\/(\d+)$/.exec(issueLink);
    let url = 'https://api.github.com/repos/' + matches[1] + '/' + matches[2] + '/issues/' + matches[3];
    this.http.get(url).take(1).subscribe(data => {
      this._cs.fundRequest('GITHUB', data['id'], issueLink, fundAmount);
    });
  }

  public async fundRequest(command: FundRequestCommand): Promise<string> {
    return this._cs.fundRequest(command.platform, command.platformId, command.link, command.amount);
  }

  public async requestQRValue(command: FundRequestCommand) {
    let body = {
      platform: command.platform,
      platformId: command.platformId,
      amount: '' + command.amount,
      url: command.link,
      fundrequestAddress: this._cs.getFundRequestContractAddress(),
      tokenAddress: this._cs.getTokenContractAddress()
    };
    return await this.http.post('/api/public/requests/0/erc67/fund', body, {responseType: 'text'}).toPromise();
  }

  public async claimRequest(command: ClaimRequestCommand): Promise<string> {
    let body = {
      platform: command.platform,
      platformId: command.platformId,
      address: await this._cs.getAccount()
    };
    await this.http.post('/api/private/requests/' + command.id + '/claim', body).take(1).subscribe((signedClaim: SignedClaim) => {
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

    add ? newWatchers.push(userId) : newWatchers.filter(function (watcher) {
      return watcher != userId;
    });

    let newRequest: IRequestRecord = createRequest(JSON.parse(JSON.stringify(request)));
    newRequest = newRequest.set('watchers', newWatchers);
    this.editRequestInStore(request, newRequest);

    let httpUrl = `/api/private/requests/${request.id}/watchers`;
    let httpCall: Observable<Object>;

    add ? httpCall = this.http.put(httpUrl, {
      responseType: 'text',
      requestId: request.id
    }) : httpCall = this.http.delete(httpUrl);

    httpCall.take(1).subscribe(null,
      error => {
        this.editRequestInStore(newRequest, request); // if something when wrong, update it back to the old value
        this.handleError(error);
      }
    );
  }

  public removeRequestInStore(request: IRequestRecord): void {
    this.store.dispatch(new RemoveRequest(request));
  }

  public addRequestInStore(newRequest: IRequestRecord): void {
    this.store.dispatch(new AddRequest(newRequest));
    this.updateRequestBalance(newRequest);
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

    typeof existingRequest == 'undefined' ? this.addRequestInStore(newOrModifiedRequest) : this.updateRequestBalance(existingRequest);
  }

  private updateRequestWithNewFundInfoFromContract(request: IRequestRecord): void {
    this._cs.getRequestFundInfo(request).then(
      (fundInfo) => {
        this.updateRequestWithNewFundInfo(request, fundInfo);
      }
    ).catch(error => {
      console.log(error);
    });
  }

  private updateRequestBalance(request: IRequestRecord): void {
    this._cs.getRequestFundInfo(request).then((fundInfo) => {
      console.log(request, fundInfo);
      this.updateRequestWithNewFundInfo(request, fundInfo);
    }).catch(error => {
      console.log(error);
    });
  }

  private updateRequestWithNewFundInfo(request: IRequestRecord, fundInfo: RequestIssueFundInformation) {
    let newRequest: IRequestRecord;

    if (!request.set) {
      newRequest = createRequest(request);
    } else {
      newRequest = request;
    }

    newRequest = newRequest.set('fundInfo', fundInfo);
    this.editRequestInStore(request, newRequest);
  }

  private handleError(error: any): void {
    console.error('An error occurred', error);
  }
}
