import {Injectable, OnDestroy} from '@angular/core';

import {IRequestRecord, RequestIssueFundInformation, SignedClaim} from '../../redux/requests.models';
import {NotificationService} from '../notification/notification.service';
import {NotificationType} from '../notification/notificationType';
import {RequestsStats} from '../../core/requests/RequestsStats';
import {Settings} from '../../core/settings/settings.model';
import {SettingsService} from '../../core/settings/settings.service';
import {Subscription} from 'rxjs/Subscription';
import {AccountWeb3Service} from '../accountWeb3/account-web3.service';
import {IAccountWeb3Record} from '../../redux/accountWeb3.models';
import swal from 'sweetalert2';

/**
 * Used to load json abi's of contracts
 */
declare let require: any;
/**
 * Window object of browser
 */
declare let window: any;
/**
 * Abi of token contract
 */
const tokenAbi = require('./tokenContract.json');
/**
 * Abi of fund request contract
 */
const fundRequestAbi = require('./fundRequestContract.json');
/**
 * Abi of fund repository contract
 */
const fundRepositoryAbi = require('./fundRepository.json');
/**
 * Abi of claim repository contract
 */
const claimRepositoryAbi = require('./claimRepository.json');

/**
 * Service that handles all calls to the smart contracts
 * @implements OnDestroy
 */
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

  /**
   * @param {SettingsService} _ss
   * @param {NotificationService} _ns
   * @param {AccountWeb3Service} _aw3s
   */
  constructor(private _ss: SettingsService,
    private _ns: NotificationService,
    private _aw3s: AccountWeb3Service) {
  }

  /**
   * Retrieve all settings from server and initialize web3 and contracts.
   * This needs be called before any other function in the service is called.
   * @returns {Promise<void>}
   */
  public async init(): Promise<void> {
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

  /**
   * @returns {string} Address of Token Contract
   */
  public getTokenContractAddress(): string {
    return this._settings.tokenContractAddress;
  }

  /**
   * @returns {string} Address of FundRequest Contract
   */
  public getFundRequestContractAddress(): string {
    return this._settings.fundRequestContractAddress;
  }

  /**
   * @returns {Promise<string>} Address of FundRepository Contract
   */
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

  /**
   * @returns {Promise<string>} Address of ClaimRepository Contract
   */
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

  /**
   * @param {string} userAddress Get FND balance of given user address, if not provided use web3 currentAccount.
   * @returns {Promise<number>} User balance in Wei
   */
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

  /**
   * @returns {Promise<number>} Total FND balance in Wei
   */
  public async getTotalBalanceInWei(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRepositoryContract.totalBalance.call(function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<number>;
  }

  /**
   * @returns {Promise<number>} Total FND balance (in Wei) that is on funded issues in Wei
   */
  public async getTotalFundedInWei(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRepositoryContract.totalFunded.call(function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<number>;
  }

  /**
   * @returns {Promise<number>} Total unique funders that have funded at least one issue
   */
  public async getTotalNumberOfFunders(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRepositoryContract.totalNumberOfFunders.call(function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<number>;
  }

  /**
   * @returns {Promise<number>} Total number of request that has funding
   */
  public async getRequestsFunded(): Promise<number> {
    return new Promise((resolve, reject) => {
      this._fundRepositoryContract.requestsFunded.call(function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<number>;
  }

  /**
   * @returns {Promise<RequestsStats>} #requests funded, #funders, total funding, total balance
   */
  public async getStatistics(): Promise<RequestsStats> {
    let stats: RequestsStats = new RequestsStats();
    let promisses = [];
    promisses.push(this.getRequestsFunded());
    promisses.push(this.getTotalNumberOfFunders());
    promisses.push(this.getTotalFundedInWei());
    promisses.push(this.getTotalBalanceInWei());

    let results: any[] = await Promise.all(promisses);
    stats.requestsFunded = results[0];
    stats.numberOfFunders = results[1];
    stats.totalAmountFunded = results[2];
    stats.totalBalance = results[3];

    return stats;
  }

  /**
   * @param {string} platform Name of platform of issue
   * @param {string} platformId Issue id on specific platform
   * @param {number} value FND to fund
   * @returns {Promise<string>} success ? "total funding by user on issue (in Wei as string)" : "-"
   */
  public async fundRequest(platform: string, platformId: string, value: number): Promise<string> {
    if (!this._accountWeb3.locked && this._accountWeb3.supported) {
      let total = this._web3.toWei(value, 'ether');
      let contractAddress = await this.getFundRequestContractAddress();
      let tx = await new Promise((resolve, reject) => {
        this._tokenContract.approveAndCall(contractAddress, total, this._web3.fromAscii(platform + '|AAC|' + String(platformId)), this._getTransactionOptions(this._accountWeb3.currentAccount), function (err, tx) {
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

  /**
   * @param {SignedClaim} signedClaim Signed Claim
   * @returns {Promise<string>} transaction string
   */
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

  /**
   * @param {IRequestRecord} request
   * @returns {Promise<RequestIssueFundInformation>} #funder, balance, current user's funding balance
   */
  public async getRequestFundInfo(request: IRequestRecord): Promise<RequestIssueFundInformation> {
    return new Promise((resolve, reject) => {
      let account = typeof this._accountWeb3.currentAccount != 'undefined' ? this._accountWeb3.currentAccount : null;
      return this._fundRepositoryContract.getFundInfo.call(this._web3.fromAscii(request.issueInformation.platform), request.issueInformation.platformId, account, function (err, result) {
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
    swal({
      title: 'Limited functionality',
      text: 'You cannot execute transactions since you are not using a Dapp browser like Mist or have MetaMask enabled',
      type: 'info'
    });
  }

  /**
   * @internal
   */
  public ngOnDestroy() {
    this._subscription.unsubscribe();
  }
}
