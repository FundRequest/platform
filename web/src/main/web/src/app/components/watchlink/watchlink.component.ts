import {Component, Input, OnInit} from '@angular/core';
import {Request} from '../../core/requests/Request';
import {UserService} from "../../core/user/user.service";
import {RequestsService} from "../../core/requests/requests.service";
import {User} from "../../core/user/User";

@Component({
  selector: 'fnd-watchlink',
  templateUrl: './watchlink.component.html',
  styleUrls: ['./watchlink.component.scss']
})
export class WatchlinkComponent implements OnInit {
  @Input() request: Request;

  private user: User;
  public userIsWatcher: boolean = false;

  constructor(private requestsService: RequestsService, private userService: UserService) {

  }

  async ngOnInit() {
    await this.initUser();
  }

  private async initUser(): Promise<void> {
    if (this.user == null) {
      this.user = await this.userService.getUserInfo() as User;
      this.userIsWatcher = this.request.watchers.includes(this.user.email);
    }
  }

  public async toggleIsWatcher(): Promise<void> {
    if (!this.userIsWatcher) {
      this.request.watchers = await this.requestsService.setUserAsWatcher(this.request, this.user) as string[];
    } else {
      this.request.watchers = await this.requestsService.unSetUserAsWatcher(this.request, this.user) as string[];
    }
    this.userIsWatcher = this.request.watchers.includes(this.user.email);
  }
}
