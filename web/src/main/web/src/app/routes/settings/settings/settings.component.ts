import {Component, OnInit} from '@angular/core';
import {UserService} from '../../../services/user/user.service';
import {IUserRecord, UserIdentity} from '../../../redux/user.models';
import {Subscription} from 'rxjs/Subscription';

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.scss']
})
export class SettingsComponent implements OnInit {
  settingActive = 1;

  private _user: IUserRecord;
  private _subscriptionUser: Subscription;

  constructor(private _us: UserService) { }

  ngOnInit() {
    this._subscriptionUser = this._us.currentUser$.subscribe((user: IUserRecord) => {
      this._user = user;
    });
  }

  public getThumbnail(account: UserIdentity) {
    switch (account.provider.toUpperCase()) {
      case 'GITHUB':
        return `https://avatars.githubusercontent.com/${account.username}`;
      default:
        return '';
    }
  }

  public get accounts(): UserIdentity[] {
    return this._user.userIdentities;
  }
}
