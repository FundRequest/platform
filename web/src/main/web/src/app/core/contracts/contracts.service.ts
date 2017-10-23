import {Injectable} from '@angular/core';
import * as Web3 from 'web3';

let tokenAbi = require('./tokenContract.json');
let fundRequestAbi = require('./fundRequestContract.json');

declare let window: any;

@Injectable()
export class ContractService {
  account: any;
  accounts: any;
  web3: any;

  tokenContractAddress: string = "0x0fcffc21d5ed3a4dd3bb99bc4b4055002e348eb0";
  fundRequestContractAddress: string = "0x78b75506895392daca4273aee6393048714da5c3";

  balance: number;
  status: string;

  tokenContract: any;
  fundRequestContract: any;

  constructor() {
    this.checkAndInstantiateWeb3();
    this.onReady();
  }

  checkAndInstantiateWeb3 = () => {
    // Checking if Web3 has been injected by the browser (Mist/MetaMask)
    if (typeof window.web3 !== 'undefined') {
      console.warn(
        'Using web3 detected from external source. If you find that your accounts don\'t appear or you have 0 MetaCoin, ensure you\'ve configured that source properly. If using MetaMask, see the following link. Feel free to delete this warning. :) http://truffleframework.com/tutorials/truffle-and-metamask'
      );
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

  onReady = () => {
    this.tokenContract = this.web3.eth.contract(tokenAbi).at(this.tokenContractAddress);
    this.fundRequestContract = this.web3.eth.contract(fundRequestAbi).at(this.fundRequestContractAddress);

    // Get the initial account balance so it can be displayed.
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
      this.accounts = accs;
      this.account = this.accounts[0];
    });
  };

  private fromWeiRounded(amountInWei) {
    let number = (amountInWei.toNumber() / 1000000000000000000);
    return ((Math.round(number * 100) / 100).toFixed(2)).toLocaleString();
  }

  public getUserBalance(callback) {
    if (this.account) {
      this.tokenContract.balanceOf.call(this.account, function (err, result) {
        let balance;
        if (result) {
          balance = this.fromWeiRounded(result);
        }
        callback(err, balance);
      });
    } else {
      callback("No accounts available");
    }
  }

  public fundRequest(requestId, value, callback) {
    let total = this.web3.toWei(value, 'ether');
    this.tokenContract.transferFunding(total, '' + requestId, function (err, result) {
      callback(err, result);
    });
    //$.post("/requests/" + requestId + "/funds", {requestId: requestId, amountInWei: total});
  }

  public getRequestBalance(requestId, callback) {
    let self = this;
    return this.fundRequestContract.balance.call('' + requestId, function (err, result) {
      let balance;
      if (result) {
        balance = self.fromWeiRounded(result);
      }
      callback(err, balance);
    });
  }
}
