import {Injectable, OnDestroy} from '@angular/core';

import * as Web3 from 'web3';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs/Observable';
import {Subscription} from 'rxjs/Subscription';
import {createAccountWeb3, IAccountWeb3Record} from '../../redux/accountWeb3.models';
import {ReplaceAccountWeb3} from '../../redux/accountWeb3.reducer';
import {IState} from '../../redux/store';
import {Settings} from '../../core/settings/settings.model';
import {SettingsService} from '../../core/settings/settings.service';

declare let window: any;

@Injectable()
export class AccountWeb3Service implements OnDestroy {
  private _nullAccount: string = '0x0000000000000000000000000000000000000000';
  private _subscription: Subscription = null;

  private _settings: Settings;

  private _accountWeb3: IAccountWeb3Record = null;

  constructor(private _ss: SettingsService, private _store: Store<IState>) { }

  public async init(): Promise<void> {
    this._settings = await this._ss.getSettings();
    this._accountWeb3 = await this._initAndGetAccountWeb3();
    this._store.dispatch(new ReplaceAccountWeb3(this._accountWeb3));
  }

  public get currentAccountWeb3$(): Observable<IAccountWeb3Record> {
    return this._store.select(state => state.accountWeb3);
  }

  public getWeb3(accountWeb3: IAccountWeb3Record): any {
    if (accountWeb3 != null) {
      let web3;
      if (typeof window.web3 != 'undefined') {
        web3 = new Web3(window.web3.currentProvider);
      } else {
        web3 = new Web3();
      }
      let networkId = accountWeb3.networkId;
      if (networkId !== '3') {
        return new Web3(new Web3.providers.HttpProvider(this._settings.providerApi));
      } else {
        return web3;
      }
    } else {
      return new Web3(new Web3.providers.HttpProvider(this._settings.providerApi));
    }
  }

  public getSelectedAccount(): string {
    let web3 = this.getWeb3CurrentProvider();
    if(web3 != null) {
      return web3.eth.accounts[0];
    } else {
      return this._nullAccount;
    }
  }

  public getWeb3CurrentProvider(): any {
    if (typeof window.web3 !== 'undefined') {
      return new Web3(window.web3.currentProvider);
    } else {
      return new Web3(new Web3.providers.HttpProvider(this._settings.providerApi));
    }
  }

  private async _initAndGetAccountWeb3(): Promise<IAccountWeb3Record> {
    let network = '';
    let networkId = '';
    let web3 = null;
    let supported = false;
    let disabled = false;
    let accountWeb3 = createAccountWeb3();
    let account;

    // Checking if Web3 has been injected by the browser (Mist/MetaMask)
    if (typeof window.web3 !== 'undefined') {
      // Use Mist/MetaMask's provider

      web3 = new Web3(window.web3.currentProvider);
      networkId = await this._getNetwork(web3);
      account = await this._getAccount(web3);

      switch (networkId) {
        case '1':
          network = 'mainnet';
          supported = false; // TODO: make available for main net ==> this._supported = true;
          break;
        case '42':
          network = 'kovan';
          supported = false;
          break;
        case '3':
          network = 'ropsten';
          supported = true;
          break;
        case '4':
          network = 'rinkeby';
          supported = false;
          break;
        default:
          supported = false;
      }
    } else {
      disabled = true;
    }

    accountWeb3 = accountWeb3.set('disabled', disabled);
    accountWeb3 = accountWeb3.set('locked', account == this._nullAccount);
    accountWeb3 = accountWeb3.set('supported', supported);
    accountWeb3 = accountWeb3.set('network', network);
    accountWeb3 = accountWeb3.set('networkId', networkId);
    accountWeb3 = accountWeb3.set('currentAccount', account);

    return accountWeb3;
  };

  private async _getNetwork(web3: any): Promise<string> {
    return await new Promise((resolve, reject) => {
      web3.version.getNetwork((err, netId) => {
        err != null ? resolve(null) : resolve(netId);
      });
    }) as string;
  }

  private async _getAccount(web3: any): Promise<string> {
    return await new Promise((resolve, reject) => {
      web3.eth.getAccounts((err, accs) => {
        err != null || accs.length === 0 ? resolve(this._nullAccount) : resolve(accs[0]);
      });
    }) as string;
  }

  public ngOnDestroy() {
    this._subscription.unsubscribe();
  }
}
