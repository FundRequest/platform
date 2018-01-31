import {Injectable, OnDestroy} from '@angular/core';

import * as swal from 'sweetalert';

import {IRequestRecord, RequestIssueFundInformation, SignedClaim} from '../../redux/requests.models';
import {NotificationService} from '../notification/notification.service';
import {NotificationType} from '../notification/notificationType';
import {RequestsStats} from '../../core/requests/RequestsStats';
import {Settings} from '../../core/settings/settings.model';
import {SettingsService} from '../../core/settings/settings.service';
import {Subscription} from 'rxjs/Subscription';
import {AccountWeb3Service} from '../accountWeb3/account-web3.service';
import {IAccountWeb3Record} from '../../redux/accountWeb3.models';


declare let require: any;
declare let window: any;

let tokenAbi = require('./tokenContract.json');
let fundRequestAbi = require('./fundRequestContract.json');
let fundRepositoryAbi = require('./fundRepository.json');
let claimRepositoryAbi = require('./claimRepository.json');

@Injectable()
export class ContractsService implements OnDestroy {

  private _tokenContract: any;
  private _fundRequestContract: any;
  private _fundRepositoryContract: any;
  private _claimRepositoryContract: any;

  private _fundRepositoryContractAddress: string = null;
  private _claimRepositoryContractAddress: string = null;

  private _settings: Settings = null;
  private _subscription: Subscription = null;

  private _web3: any = null;
  private _accountWeb3: IAccountWeb3Record = null;

  constructor(private _ss: SettingsService, private _ns: NotificationService, private _aw3s: AccountWeb3Service) {
  }

  public async init() {
    this._settings = await this._ss.getSettings();
    this._subscription = this._aw3s.currentAccountWeb3$.subscribe((accountWeb3: IAccountWeb3Record) => {
      this._accountWeb3 = accountWeb3;
      this._web3 = this._aw3s.getWeb3(this._accountWeb3);
    });
    await this.setContracts();
  }

  private async setContracts() {
    this._tokenContract = this._web3.eth.contract(tokenAbi).at(this.getTokenContractAddress());
    this._fundRequestContract = this._web3.eth.contract(fundRequestAbi).at(this.getFundRequestContractAddress());

    this._fundRepositoryContract = this._web3.eth.contract(fundRepositoryAbi).at(await this.getFundRepositoryContractAddress());
    this._claimRepositoryContract = this._web3.eth.contract(claimRepositoryAbi).at(await this.getClaimRepositoryContractAddress());
  };

  public getTokenContractAddress(): string {
    return this._settings.tokenContractAddress;
  }

  public getFundRequestContractAddress(): string {
    return this._settings.fundRequestContractAddress;
  }

  public async getFundRepositoryContractAddress(): Promise<string> {
    if (this._fundRepositoryContractAddress == null) {
      this._fundRepositoryContractAddress = await new Promise((resolve, reject) => {
        this._fundRequestContract.fundRepository.call(function (err, result) {
          err ? reject(err) : resolve(result);
        });
      }) as string;
    }
    return this._fundRepositoryContractAddress;
  }

  public async getClaimRepositoryContractAddress(): Promise<string> {
    if (this._claimRepositoryContractAddress == null) {
      this._claimRepositoryContractAddress = await new Promise((resolve, reject) => {
        this._fundRequestContract.claimRepository.call(function (err, result) {
          err ? reject(err) : resolve(result);
        });
      }) as string;
    }
    return this._claimRepositoryContractAddress;
  }

  public async getUserBalance(userAddress?: string): Promise<number> {
    if (!this._accountWeb3.locked && this._accountWeb3.supported) {
      let address = userAddress || this._accountWeb3.currentAccount;
      return new Promise((resolve, reject) => {
        this._tokenContract.balanceOf.call(address, function (err, result) {
          err ? reject(err) : resolve(result);
        });
      }) as Promise<number>;
    } else {
      return Promise.resolve(0);
    }
  }

  public async getTotalBalanceInWei(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRepositoryContract.totalBalance.call(function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<number>;
  }

  public async getTotalFundedInWei(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRepositoryContract.totalFunded.call(function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<number>;
  }

  public async getTotalNumberOfFunders(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRepositoryContract.totalNumberOfFunders.call(function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<number>;
  }

  public async getRequestsFunded(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRepositoryContract.requestsFunded.call(function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<number>;
  }

  public async getStatistics(): Promise<RequestsStats> {
    let stats: RequestsStats = new RequestsStats();
    stats.requestsFunded = await this.getRequestsFunded();
    stats.numberOfFunders = await this.getTotalNumberOfFunders();
    stats.totalAmountFunded = await this.getTotalFundedInWei();
    stats.totalBalance = await this.getTotalBalanceInWei();

    return stats;
  }

  public async fundRequest(platform: string, platformId: string, value: number): Promise<string> {
    if (!this._accountWeb3.locked && this._accountWeb3.supported) {
      let total = this._web3.toWei(value, 'ether');
      let tx = await new Promise((resolve, reject) => {
        this._tokenContract.approveAndCall(this.getFundRequestContractAddress(), total, this._web3.fromAscii(platform + '|AAC|' + String(platformId)), this._getTransactionOptions(this._accountWeb3.currentAccount), function (err, tx) {
          err ? reject(err) : resolve(tx);
        });
      }) as string;

      this._ns.message(NotificationType.FUND_SUCCESS, this._getTransactionLink(tx));
      return Promise.resolve(total);
    } else {
      this._showLimitedFunctionalityAlert();
      return Promise.resolve('-');
    }
  }

  public async claimRequest(signedClaim: SignedClaim): Promise<string> {
    return await new Promise((resolve, reject) => {
      this._fundRequestContract.claim.sendTransaction(
        this._web3.fromAscii(signedClaim.platform),
        this._web3.fromAscii(signedClaim.platformId),
        signedClaim.solver,
        signedClaim.solverAddress,
        signedClaim.r,
        signedClaim.s,
        signedClaim.v, this._getTransactionOptions(this._accountWeb3.currentAccount), function (err, tx) {
          err ? reject(err) : resolve(tx);
        });
    }) as string;
  }

  public async getRequestFundInfo(request: IRequestRecord): Promise<RequestIssueFundInformation> {
    return new Promise((resolve, reject) => {
      return this._fundRepositoryContract.getFundInfo.call(this._web3.fromAscii(request.issueInformation.platform), String(request.issueInformation.platformId), this._accountWeb3.currentAccount, function (err, result) {
        err ? reject(err) : resolve({numberOfFunders: result[0], balance: result[1], funderBalance: result[2]});
      });
    }) as Promise<RequestIssueFundInformation>;
  }

  private _getTransactionLink(tx: string): string {
    return `<a target="_blank" href="${this._settings.etherscan}tx/${tx}">Go to transaction.</a>`;
  }

  private _getTransactionOptions(account: string): any {
    return {
      from: account,
      gas: 350000
    };
  }

  private _showLimitedFunctionalityAlert() {
    swal('Limited functionality',
      'You cannot execute transactions since you are not using a Dapp browser like Mist or have MetaMask enabled', 'error'
    );
  }

  public ngOnDestroy() {
    this._subscription.unsubscribe();
  }
}
