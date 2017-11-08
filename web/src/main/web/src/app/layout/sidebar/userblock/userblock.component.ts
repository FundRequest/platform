import {Component} from "@angular/core";

import {UserblockService} from "./userblock.service";
import {UserService} from "../../../services/user/user.service";
import {IUserRecord} from "../../../redux/user.models";

@Component({
  selector: 'app-userblock',
  templateUrl: './userblock.component.html',
  styleUrls: ['./userblock.component.scss']
})
export class UserblockComponent {
  public user: IUserRecord;
  balance: number;
  allowance: number;

  constructor(public userService: UserService,
              public userblockService: UserblockService) {
    userService.getCurrentUser().subscribe((user: IUserRecord) => {
      this.user = user;
    });
  }

  userBlockIsVisible(): boolean {
    return this.userblockService.getVisibility();
  }

}
