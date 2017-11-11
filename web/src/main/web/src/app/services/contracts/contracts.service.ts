import {Injectable} from '@angular/core';
import * as Web3 from 'web3';
import {IRequestRecord} from "../../redux/requests.models";

let tokenAbi = require('./tokenContract.json');
let fundRequestAbi = require('./fundRequestContract.json');

declare let window: any;

@Injectable()
export class ContractsService {
  account: string;
  web3: any;

  tokenContractAddress: string = "0xbc84f3bf7dd607a37f9e5848a6333e6c188d926c";
  fundRequestContractAddress: string = "0xa505ef7aad27f757fddbc2d3f875e28d4a75050b";

  status: string;

  tokenContract: any;
  fundRequestContract: any;

  constructor() {
    this.checkAndInstantiateWeb3();
    this.setContracts();
    this.initVars();
  }

  private checkAndInstantiateWeb3(): void {
    // Checking if Web3 has been injected by the browser (Mist/MetaMask)
    if (typeof window.web3 !== 'undefined') {
      // Use Mist/MetaMask's provider
      this.web3 = new Web3(window.web3.currentProvider);

      if (this.web3.version.network !== '4') {
        alert('Please connect to the Rinkeby network');
      }
    } else {
      console.warn(
        'Please use a dapp browser like mist or MetaMask plugin for chrome'
      );
    }
  };

  private setContracts(): void {
    this.tokenContract = this.web3.eth.contract(tokenAbi).at(this.tokenContractAddress);
    this.fundRequestContract = this.web3.eth.contract(fundRequestAbi).at(this.fundRequestContractAddress);
  };

  private async initVars(): Promise<void> {
    this.getAccount().then(account => {
      this.account = account;
      this.web3.eth.defaultAccount = account;
    });
  }

  private static fromWeiRounded(amountInWei: number): number {
    let number = amountInWei / 1000000000000000000;
    return (Math.round(number * 100) / 100);
  }

  private getAccount(): Promise<string> {
    return new Promise((resolve, reject) => {
      this.web3.eth.getAccounts((err, accs) => {
        if (err != null) {
          alert('There was an error fetching your accounts.');
          return;
        }

        if (accs.length === 0) {
          alert(
            'Couldn\'t get any accounts! Make sure your Ethereum client is configured correctly.'
          );
          return;
        }
        resolve(accs[0])
      })
    });
  }

  public async getUserBalance(): Promise<number> {
    console.log('before init getUserBalance', this.account, this.fundRequestContractAddress);
    if (this.account == null) {
      await this.initVars();
    }
    console.log('after init getUserBalance',this.account, this.fundRequestContractAddress);

    return new Promise((resolve, reject) => {
      this.tokenContract.balanceOf.call(this.account, function (err, result) {
        let balance: number;
        if (+result > 0) {
          balance = ContractsService.fromWeiRounded(+result);
        }

        resolve(balance);
      });
    }) as Promise<number>;
  }

  public async getUserAllowance(): Promise<number> {
    if (this.account == null) {
      await this.initVars();
    }

    return new Promise((resolve, reject) => {
      this.tokenContract.allowance.call(this.account, this.fundRequestContractAddress, function (err, result) {
        let allowance: number;
        if (+result > 0) {
          allowance = ContractsService.fromWeiRounded(+result);
        }

        resolve(allowance);
      });
    }) as Promise<number>;
  }

  public setUserAllowance(value: number): Promise<number> {
    return new Promise((resolve, reject) => {
      let total = this.web3.toWei(value, 'ether');
      return this.tokenContract.approve(this.account, total, function (err, result) {
        if (err) {
          reject(err);
        } else {
          // TODO: save transaction address (result)
          resolve(value);
        }
      });
    });
  }

  public fundRequest(request: IRequestRecord, value: number): Promise<number> {
    return new Promise((resolve, reject) => {
      let total = this.web3.toWei(value, 'ether');
      return this.fundRequestContract.fund(total, String(request.id), '', function (err, result) {
        if (err) {
          reject(err);
        } else {
          resolve(request.balance + value)
        }
      });
    });
  }

  public getRequestBalance(request: IRequestRecord): Promise<number> {
    return new Promise((resolve, reject) => {
      return this.fundRequestContract.balance.call(request.id, function (err, result) {
        let balance;
        if (result) {
          balance = ContractsService.fromWeiRounded(result);
        }
        resolve(balance);
      });
    });
  }
}
