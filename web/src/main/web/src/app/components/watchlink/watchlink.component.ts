import {Component, Input, OnInit} from '@angular/core';
import {UserService} from "../../core/user/user.service";
import {User} from "../../core/user/User";
import {IRequestRecord, RequestService} from "../../services/request/request.service";

@Component({
  selector: 'fnd-watchlink',
  templateUrl: './watchlink.component.html',
  styleUrls: ['./watchlink.component.scss']
})
export class WatchlinkComponent implements OnInit {
  @Input() request: IRequestRecord;

  private user: User;
  public userIsWatcher: boolean = false;

  constructor(private requestService: RequestService, private userService: UserService) {

  }

  async ngOnInit() {
    await this.initUser();
    this.requestService
      .requests.map(list => list.filter(request => request.id == this.request.id).toList())
      .subscribe(list => {
        this.request = list.first();
        this.userIsWatcher = this.request.watchers.includes(this.user.email);
      });
  }

  private async initUser(): Promise<void> {
    if (this.user == null) {
      this.user = await this.userService.getUserInfo() as User;
      this.userIsWatcher = this.request.watchers.includes(this.user.email);
    }
  }

  public toggleIsWatcher(): void {
    if (!this.userIsWatcher) {
      this.requestService.setUserAsWatcher(this.request, this.user);
    } else {
      this.requestService.unSetUserAsWatcher(this.request, this.user);
    }
  }
}
