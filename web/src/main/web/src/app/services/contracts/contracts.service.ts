import {Injectable} from '@angular/core';
import * as Web3 from 'web3';
import {IRequestRecord} from '../../redux/requests.models';
import {NotificationService} from '../notification/notification.service';


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

  private _init: boolean = false;

  private _tokenContractAddress: string = '0x441e36bc87d343e7b2f908570823b43ac4ef6cb6';
  private _fundRequestContractAddress: string = '0x43a29f127adbc1e664c389367b0a0fceee36e764';

  constructor(private _ns: NotificationService) {
    if (!this._init) {
      this.init();
    }
  }

  public async init() {
    await this.checkAndInstantiateWeb3();
    if (this._web3) {
      this.setContracts();
      this.getAccount();
    }
  }

  private async checkAndInstantiateWeb3() {
    // Checking if Web3 has been injected by the browser (Mist/MetaMask)
    if (typeof window.web3 !== 'undefined') {
      // Use Mist/MetaMask's provider
      this._web3 = new Web3(window.web3.currentProvider);
      let netId = await this.getNetwork();
      if (netId !== '4') {
        this._web3 = new Web3(new Web3.providers.HttpProvider('https://rinkeby.infura.io'));
      }
    } else {
      this._web3 = new Web3(new Web3.providers.HttpProvider('https://rinkeby.infura.io'));
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
    let account = await this.getAccount();

    return new Promise((resolve, reject) => {
      this._tokenContract.balanceOf.call(account, function (err, result) {
        if (err) {
          reject(err);
        }

        resolve(result);
      });
    }) as Promise<number>;
  }

  public async getUserAllowance(): Promise<string> {
    let account = await this.getAccount();

    return new Promise((resolve, reject) => {
      this._tokenContract.allowance.call(account, this._fundRequestContractAddress, function (err, result) {
        err ? reject(err) : resolve(result);
      });
    }) as Promise<string>;
  }

  public async setUserAllowance(value: number): Promise<string> {
    let account: string = await this.getAccount();

    if (account != null) {
      let currentAllowance: string = await this.getUserAllowance();
      let total = this._web3.toWei(value, 'ether');

      let tx = await new Promise((resolve, reject) => {
        this._tokenContract.safeApprove.sendTransaction(this._fundRequestContractAddress, currentAllowance, total, this._getTransactionOptions(account), function (err, tx) {
          err ? reject(err) : resolve(tx);
        });
      }) as string;

      this._ns.success('Transaction \'set allowance\' sent.', this._getTransactionLink(tx));
      return Promise.resolve(total);
    } else {
      this.showLimitedFunctionalityAlert();
      return Promise.resolve('0');
    }
  }

  private showLimitedFunctionalityAlert() {
    swal('Limited functionality',
      'You cannot execute transactions since you are not using a Dapp browser like Mist or have MetaMask enabled', 'error'
    );
  }

  public async fundRequest(request: IRequestRecord, value: number): Promise<string> {
    let account: string = await this.getAccount();
    if (!!account) {
      let currentAllowance: string = await this.getUserAllowance();
      let total = this._web3.toWei(value, 'ether');

      if (+total > +currentAllowance) {
        await new Promise((resolve, reject) => {
          let batch = this._web3.createBatch();
          batch.add(this._tokenContract.approve.request(this._fundRequestContractAddress, currentAllowance, total, this._getTransactionOptionsForBatch(account), function (err, result) {
            err ? reject(err) : console.log('approve result: ', result);
          }));
          batch.add(this._fundRequestContract.fund.request(total, this._web3.fromAscii(String(request.id)), account, this._getTransactionOptionsForBatch(account), function (err, result) {
            err ? reject(err) : resolve(total);
          }));
          batch.execute();
        });
        // TODO: Check if there is a way to get transaction hashes of batch
      }
      else {
        let tx = await new Promise((resolve, reject) => {
          this._fundRequestContract.fund.sendTransaction(total, this._web3.fromAscii(String(request.id)), account, this._getTransactionOptions(account), function (err, tx) {
            err ? reject(err) : resolve(tx);
          });
        }) as string;

        this._ns.success('Transaction \'fund request\' sent.', this._getTransactionLink(tx));
      }

      return Promise.resolve(total);
    } else {
      this.showLimitedFunctionalityAlert();
      return Promise.resolve('0');
    }

  }

  public getRequestBalance(request: IRequestRecord): Promise<string> {
    return new Promise((resolve, reject) => {
      return this._fundRequestContract.balance.call(this._web3.fromAscii(String(request.id)), function (err, result) {
        if (err) {
          reject(err);
        } else {
          resolve(result);
        }
      });
    }) as Promise<string>;
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
      gas: 300000
    };
  }
}
