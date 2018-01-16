import {Injectable} from '@angular/core';
import * as Web3 from 'web3';
import {FundInfo, IRequestRecord, SignedClaim} from '../../redux/requests.models';
import {NotificationService} from '../notification/notification.service';


const swal = require('sweetalert');

declare let require: any;
declare let window: any;

let tokenAbi = require('./tokenContract.json');
let fundRequestAbi = require('./fundRequestContract.json');

@Injectable()
export class ContractsService {
  public _account: string = null;
  private _web3: any;

  private _tokenContract: any;
  private _fundRequestContract: any;

  private _init: boolean = false;

  private _tokenContractAddress: string = '0xfd1de38dc456112c55c3e6bc6134b2f545b91386';
  private _fundRequestContractAddress: string = '0x797b33d3bb0c74a7860cd2ca80bf063809dced80';

  constructor(private _ns: NotificationService) {
  }

  public async init() {
    if (!this._init) {
      await this.checkAndInstantiateWeb3();
      if (this._web3) {
        this.setContracts();
        await this.getAccount();
      }
      this._init = true;
    }
  }

  private async checkAndInstantiateWeb3() {
    // Checking if Web3 has been injected by the browser (Mist/MetaMask)
    if (typeof window.web3 !== 'undefined') {
      // Use Mist/MetaMask's provider
      this._web3 = new Web3(window.web3.currentProvider);
      if (await this.getNetwork() !== '3') {
        this._web3 = new Web3(new Web3.providers.HttpProvider('https://ropsten.infura.io/'));
      }
    } else {
      this._web3 = new Web3(new Web3.providers.HttpProvider('https://ropsten.infura.io/'));
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
          // alert('There was an error fetching your accounts.');
          resolve(null);
          return;
        }
        resolve(netId);
      });
    }) as string;
  }

  private async getAccount(): Promise<string> {
    if (this._account == null) {
      this._account = await new Promise((resolve, reject) => {
        this._web3.eth.getAccounts((err, accs) => {
          if (err != null) {
            // alert('There was an error fetching your accounts.');
            resolve(null);
            return;
          }

          if (accs.length === 0) {
            resolve(null);
            return;
          }
          resolve(accs[0]);
        });
      }) as string;

      this._web3.eth.defaultAccount = this._account;
    }

    return Promise.resolve(this._account);
  }

  public async getUserBalance(): Promise<number> {
    if (!this._init) {
      await this.init();
    }

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

  private showLimitedFunctionalityAlert() {
    swal('Limited functionality',
      'You cannot execute transactions since you are not using a Dapp browser like Mist or have MetaMask enabled', 'error'
    );
  }

  public async fundRequest(platform: string, platformId: string, url: string, value: number): Promise<string> {
    if (!this._init) {
      await this.init();
    }

    if (!!this._account) {
      let total = this._web3.toWei(value, 'ether');
      let tx = await new Promise((resolve, reject) => {
        console.log(this._fundRequestContractAddress);
        console.log(total);
        console.log(platform + "|" + String(platformId) + "|" + url);
        this._tokenContract.approveAndCall(this._fundRequestContractAddress, total, this._web3.fromAscii(platform + "|" + String(platformId) + "|" + url), this._getTransactionOptions(this._account), function (err, tx) {
          err ? reject(err) : resolve(tx);
        });
      }) as string;

      this._ns.success('Transaction \'fund request\' sent.', this._getTransactionLink(tx));

      return Promise.resolve(total);
    } else {
      this.showLimitedFunctionalityAlert();
      return Promise.resolve('0');
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

  public async getRequestFundInfo(request: IRequestRecord): Promise<FundInfo> {
    if (!this._init) {
      await this.init();
    }
    return new Promise((resolve, reject) => {
      return this._fundRequestContract.getFundInfo.call(this._web3.fromAscii(request.issueInformation.platform), this._web3.fromAscii(String(request.issueInformation.platformId)), this._account, function (err, result) {
        if (err) {
          reject(err);
        } else {
          if (result) {
            resolve(new FundInfo(result[0], result[1], result[2]));
          }
        }
      });
    }) as Promise<FundInfo>;
  }

  private _getTransactionLink(tx: string): string {
    // TODO: Parametrize link
    return `<a target="_blank" href="https://rinkeby.etherscan.io/tx/${tx}">Go to transaction.</a>`;
  }

  private _getTransactionOptionsForBatch(account: string): any {
    return {
      from: account,
      gas: 1
    };
  }

  private _getTransactionOptions(account: string): any {
    return {
      from: account,
      gas: 350000
    };
  }
}
