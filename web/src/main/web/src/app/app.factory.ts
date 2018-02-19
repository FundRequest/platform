import {Injectable} from '@angular/core';
import {ContractsService} from './services/contracts/contracts.service';
import {AccountWeb3Service} from './services/accountWeb3/account-web3.service';
import {Store} from '@ngrx/store';
import {IState} from './redux/store';
import {IAccountWeb3Record} from './redux/accountWeb3.models';
import {ReplaceAccountWeb3} from './redux/accountWeb3.reducer';
import {Observable} from 'rxjs/Rx';

@Injectable()
export class AppFactory {
  private _accountWeb3: IAccountWeb3Record;
  private _firstTime: boolean = true;
  private _asyncBusy: boolean = false;

  constructor(private _cs: ContractsService, private _aw3s: AccountWeb3Service, private _store: Store<IState>) {
  }

  public async load(): Promise<boolean> {
    await this._aw3s.init();
    await this._cs.init();
    this._aw3s.currentAccountWeb3$.subscribe((accountWeb3: IAccountWeb3Record) => {
      this._accountWeb3 = accountWeb3;
      this._setAccountRefreshInterval();
    });

    return true;
  }

  private _setAccountRefreshInterval(): void {
    if (this._firstTime) {
      // timer to check if metamask changed, update address and status
      Observable.timer(0, 1000).subscribe((t) => {
        let account = this._aw3s.getSelectedAccount();
        if (
          !this._asyncBusy && (this._accountWeb3.currentAccount != account || this._firstTime)
        ) {
          if (typeof account != 'undefined' && account != '0x0000000000000000000000000000000000000000') {
            this._accountWeb3 = this._accountWeb3.set('currentAccount', account);
            this._accountWeb3 = this._accountWeb3.set('locked', false);

            this._asyncBusy = true;
            this._cs.getUserBalance(account).then(balance => {
              this._accountWeb3 = this._accountWeb3.set('balance', balance);
              this._store.dispatch(new ReplaceAccountWeb3(this._accountWeb3));
              this._asyncBusy = false;
            }).catch(() => {
              this._accountWeb3 = this._accountWeb3.set('balance', 0);
              this._store.dispatch(new ReplaceAccountWeb3(this._accountWeb3));
              this._asyncBusy = false;
            });
          } else {
            this._accountWeb3 = this._accountWeb3.set('currentAccount', account);
            this._accountWeb3 = this._accountWeb3.set('locked', true);
            this._accountWeb3 = this._accountWeb3.set('balance', 0);
            this._store.dispatch(new ReplaceAccountWeb3(this._accountWeb3));
          }
        }
        this._firstTime = false;
      });
    }
  }
}
