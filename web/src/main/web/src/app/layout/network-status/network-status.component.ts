import {Component, OnDestroy, OnInit} from '@angular/core';
import {IAccountWeb3Record} from '../../redux/accountWeb3.models';
import {AccountWeb3Service} from '../../services/accountWeb3/account-web3.service';
import {Subscription} from 'rxjs/Subscription';
import {Utils} from '../../shared/utils';
import {SettingsService} from '../../core/settings/settings.service';

@Component({
  selector: 'app-network-status',
  templateUrl: './network-status.component.html',
  styleUrls: ['./network-status.component.scss']
})
export class NetworkStatusComponent implements OnInit, OnDestroy {

  private _subscriptionAccountWeb3: Subscription;
  private _accountWeb3: IAccountWeb3Record;

  constructor(private _aw3s: AccountWeb3Service, private _settings: SettingsService) {
  }

  ngOnInit() {
    this._subscriptionAccountWeb3 = this._aw3s.currentAccountWeb3$.subscribe((accountWeb3: IAccountWeb3Record) => {
      this._accountWeb3 = accountWeb3;
    });
  }

  public get isCollapsed() {
    return this._settings.layout.isCollapsed;
  }

  public goToMetamask() {
    Utils.openExternalUrl( 'https://metamask.io');
  }

  public get accountWeb3(): IAccountWeb3Record {
    return this._accountWeb3;
  }

  public ngOnDestroy() {
    this._subscriptionAccountWeb3.unsubscribe();
  }

}
