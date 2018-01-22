import {Injectable} from '@angular/core';
import * as Web3 from 'web3';
import {IRequestRecord, RequestIssueFundInformation, SignedClaim} from '../../redux/requests.models';
import {NotificationService} from '../notification/notification.service';
import {SettingsService} from '../../core/settings/settings.service';
import {NotificationType} from '../notification/notificationType';
import { RequestsStats } from '../../core/requests/RequestsStats';
import { Utils } from '../../shared/utils';


const swal = require('sweetalert');

declare let require: any;
declare let window: any;

let tokenAbi = require('./tokenContract.json');
let fundRequestAbi = require('./fundRequestContract.json');

@Injectable()
export class ContractsService {
  private _account: string = null;
  private _web3: any;

  private _tokenContract: any;
  private _fundRequestContract: any;

  private _tokenContractAddress: string = '0xfd1de38dc456112c55c3e6bc6134b2f545b91386';
  private _fundRequestContractAddress: string = '0x797b33d3bb0c74a7860cd2ca80bf063809dced80';

  private _limited: boolean = true;
  private _providerApi = 'https://ropsten.davyvanroy.be/';
  private _etherscan = 'https://ropsten.etherscan.io/';

  constructor(private _settings: SettingsService, private _ns: NotificationService) {
  }

  public async init() {
    await this.checkAndInstantiateWeb3();
    if (this._web3) {
      this.setContracts();
      await this.getAccount();
    }
  }

  public get limited(): boolean {
    return this._limited;
  }

  private async checkAndInstantiateWeb3() {
    // Checking if Web3 has been injected by the browser (Mist/MetaMask)
    if (typeof window.web3 !== 'undefined') {
      // Use Mist/MetaMask's provider
      this._web3 = new Web3(window.web3.currentProvider);
      if (await this.getNetwork() !== '3') {
        this._web3 = new Web3(new Web3.providers.HttpProvider(this._providerApi));
      }
    } else {
      this._web3 = new Web3(new Web3.providers.HttpProvider(this._providerApi));
    }
  };

  private setContracts(): void {
    this._tokenContract = this._web3.eth.contract(tokenAbi).at(this._tokenContractAddress);
    this._fundRequestContract = this._web3.eth.contract(fundRequestAbi).at(this._fundRequestContractAddress);
  };

  private async getNetwork(): Promise<string> {
    return await new Promise((resolve, reject) => {
      this._web3.version.getNetwork((err, netId) => {
        if (err != null) {
          resolve(null);
          return;
        }
        resolve(netId);
      });
    }) as string;
  }

  public getTokenContractAddress(): string {
    return this._tokenContractAddress;
  }

  public getFundRequestContractAddress(): string {
    return this._fundRequestContractAddress;
  }

  public async getAccount(): Promise<string> {
    if (this._account == null) {
      this._account = await new Promise((resolve, reject) => {
        this._web3.eth.getAccounts((err, accs) => {
          if (err != null || accs.length === 0) {
            this.showLimitedFunctionalityAlert();
            resolve(null);
            return;
          }

          this._limited = false;
          resolve(accs[0]);
        });
      }) as string;

      this._web3.eth.defaultAccount = this._account;
    }

    return Promise.resolve(this._account);
  }

  public async getUserBalance(): Promise<number> {
    if (this._account != null) {
      return new Promise((resolve, reject) => {
        this._tokenContract.balanceOf.call(this._account, function (err, result) {
          if (err) {
            reject(err);
          }

          resolve(result);
        });
      }) as Promise<number>;
    } else {
      return new Promise((resolve, reject) => {
        resolve(0);
      }) as Promise<number>;
    }
  }

  public async getTotalBalanceInWei(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRequestContract.totalBalance.call(function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<number>;
  }

  public async getTotalFundedInWei(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRequestContract.totalFunded.call(function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<number>;
  }

  public async getTotalNumberOfFunders(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRequestContract.totalNumberOfFunders.call(function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<number>;
  }

  public async getRequestsFunded(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRequestContract.requestsFunded.call(function (err, result) {
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

  private showLimitedFunctionalityAlert() {
    swal('Limited functionality',
      'You cannot execute transactions since you are not using a Dapp browser like Mist or have MetaMask enabled', 'error'
    );
  }

  public async fundRequest(platform: string, platformId: string, url: string, value: number): Promise<string> {
    if (!!this._account) {
      let total = this._web3.toWei(value, 'ether');
      let tx = await new Promise((resolve, reject) => {
        this._tokenContract.approveAndCall(this._fundRequestContractAddress, total, this._web3.fromAscii(platform + "|" + String(platformId) + "|" + url), this._getTransactionOptions(this._account), function (err, tx) {
          err ? reject(err) : resolve(tx);
        });
      }) as string;

      this._ns.message(NotificationType.FUND_SUCCESS, this._getTransactionLink(tx));
      return Promise.resolve(total);
    } else {
      this.showLimitedFunctionalityAlert();
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
        signedClaim.v, this._getTransactionOptions(this._account), function (err, tx) {
          err ? reject(err) : resolve(tx);
        });
    }) as string;
  }

  public async getRequestFundInfo(request: IRequestRecord): Promise<RequestIssueFundInformation> {
    return new Promise((resolve, reject) => {
      return this._fundRequestContract.getFundInfo.call(this._web3.fromAscii(request.issueInformation.platform), this._web3.fromAscii(String(request.issueInformation.platformId)), this._account, function (err, result) {
        err ? reject(err) : resolve({numberOfFunders: result[0], balance: result[1], funderBalance: result[2]});
      });
    }) as Promise<RequestIssueFundInformation>;
  }

  private _getTransactionLink(tx: string): string {
    return `<a target="_blank" href="${this._etherscan}tx/${tx}">Go to transaction.</a>`;
  }

  private _getTransactionOptions(account: string): any {
    return {
      from: account,
      gas: 350000
    };
  }
}
