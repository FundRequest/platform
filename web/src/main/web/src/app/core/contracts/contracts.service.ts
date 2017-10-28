import {Injectable} from '@angular/core';
import {Request} from "../requests/request";
import * as Web3 from 'web3';

let tokenAbi = require('./tokenContract.json');
let fundRequestAbi = require('./fundRequestContract.json');

declare let window: any;

@Injectable()
export class ContractService {
  account: string;
  web3: any;

  tokenContractAddress: string = "0x0fcffc21d5ed3a4dd3bb99bc4b4055002e348eb0";
  fundRequestContractAddress: string = "0x78b75506895392daca4273aee6393048714da5c3";

  balance: string;
  status: string;

  tokenContract: any;
  fundRequestContract: any;

  constructor() {
    this.checkAndInstantiateWeb3();
    this.setContracts();
  }

  private checkAndInstantiateWeb3(): void {
    // Checking if Web3 has been injected by the browser (Mist/MetaMask)
    if (typeof window.web3 !== 'undefined') {
      // Use Mist/MetaMask's provider
      this.web3 = new Web3(window.web3.currentProvider);

      if (this.web3.version.network !== '4') {
        alert('Please connect to the Rinkeby network');
        //TODO redirect to a page with some extra explanation
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

  private init(): Promise<any> {
    return this.getAccount()
      .then(account => {
        this.account = account as string;
        this.web3.eth.defaultAccount = account
      })
      .then(account => this.getUserBalanceAsString())
      .then(balance => this.balance = balance);
  }

  public async getUserBalance(): Promise<string> {
    if (!this.balance) {
      await this.init();
    }
    return Promise.resolve(this.balance);
  }

  private static fromWeiRounded(amountInWei): string {
    let number = (amountInWei.toNumber() / 1000000000000000000);
    return ((Math.round(number * 100) / 100).toFixed(2)).toLocaleString();
  }

  private getAccount(): Promise<any> {
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

  private getUserBalanceAsString(): Promise<string> {
    return new Promise((resolve, reject) => {
      if (this.account) {
        this.tokenContract.balanceOf.call(this.account, function (err, result) {
          let balance;
          if (result) {
            balance = ContractService.fromWeiRounded(result);
          }
          resolve(balance);
        });
      } else {
        reject('problem retrieving balance');
      }
    })
  }

  public async fundRequest(request: Request, value) {
    return new Promise((resolve, reject) => {
      let total = this.web3.toWei(value, 'ether');
      return this.tokenContract.transferFunding(total, String(request.id), function (err, result) {
        if (err) {
          reject(err);
        } else {
          request.balance = (Number.parseFloat(request.balance) + value).toFixed(2).toLocaleString();
          // TODO: save transaction address (result)
          resolve(request);
        }
      });
    });
  }

  public getRequestBalance(requestId: string): Promise<string> {
    return new Promise((resolve, reject) => {
      return this.fundRequestContract.balance.call(requestId, function (err, result) {
        let balance;
        if (result) {
          balance = ContractService.fromWeiRounded(result);
        }
        resolve(balance);
      });
    });
  }
}
