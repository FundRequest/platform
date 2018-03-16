import {Component, OnDestroy, OnInit} from '@angular/core';

import {UserblockService} from './userblock.service';
import {UserService} from '../../services/user/user.service';
import {IUserRecord} from '../../redux/user.models';
import {AccountWeb3Service} from '../../services/accountWeb3/account-web3.service';
import {IAccountWeb3Record} from '../../redux/accountWeb3.models';
import {Subscription} from 'rxjs/Subscription';

@Component({
  selector: 'app-userblock',
  templateUrl: './userblock.component.html',
  styleUrls: ['./userblock.component.scss']
})
export class UserblockComponent implements OnInit, OnDestroy {
  private _user: IUserRecord;
  private _accountWeb3: IAccountWeb3Record;
  private _subscriptionUser: Subscription;
  private _subscriptionAccountWeb3: Subscription;

  constructor(private _us: UserService,
    private _ubs: UserblockService,
    private _aw3s: AccountWeb3Service) {
  }

  public ngOnInit() {
    this._subscriptionUser = this._us.currentUser$.subscribe((user: IUserRecord) => {
      this._user = user;
    });
    this._subscriptionAccountWeb3 = this._aw3s.currentAccountWeb3$.subscribe((accountWeb3: IAccountWeb3Record) => {
      this._accountWeb3 = accountWeb3;
    });
  }

  public get user() {
    return this._user;
  }

  public get accountWeb3() {
    return this._accountWeb3;
  }

  public login(): void {
    this._us.login();
  }

  public logout($event): void {
    $event.preventDefault();
    this._us.logout();
  }

  public userIsLoggedIn(): boolean {
    return this._us.userIsLoggedIn();
  }

  public userBlockIsVisible(): boolean {
    return this._ubs.getVisibility();
  }

  public ngOnDestroy() {
    this._subscriptionUser.unsubscribe();
    this._subscriptionAccountWeb3.unsubscribe();
  }

}
