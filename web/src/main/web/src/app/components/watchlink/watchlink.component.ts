import {Component, Input} from '@angular/core';
import {IUserRecord} from '../../redux/user.models';
import {IRequestRecord} from '../../redux/requests.models';
import {RequestService} from '../../services/request/request.service';
import {AuthService} from '../../core/auth/auth.service';

@Component({
  selector: 'fnd-watchlink',
  templateUrl: './watchlink.component.html',
  styleUrls: ['./watchlink.component.scss']
})
export class WatchlinkComponent {
  @Input() user: IUserRecord;
  @Input() request: IRequestRecord;

  constructor(private _rs: RequestService, private _as: AuthService) {
  }

  public get isLoggedIn(): boolean {
    return this._as.isAuthenticated();
  }

  public get isWatcher(): boolean {
    return this.request.watchers.indexOf(this.user.email) > -1;
  }

  public get first10watchers(): string[] {
    return this.request.watchers.filter((item: string, index: number) => index < 10);
  }

  public get hasMoreWatchers(): boolean {
    return this.request.watchers.length > 10;
  }

  public toggleIsWatcher(): void {
    if (this.isLoggedIn) {
      if (!this.isWatcher) {
        this._rs.setUserAsWatcher(this.request, this.user);
      } else {
        this._rs.unSetUserAsWatcher(this.request, this.user);
      }
    }
  }
}
