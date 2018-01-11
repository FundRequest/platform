import {Component, OnInit} from '@angular/core';

import {UserblockService} from './userblock.service';
import {UserService} from '../../../services/user/user.service';
import {IUserRecord} from '../../../redux/user.models';
import {BsModalRef} from 'ngx-bootstrap';

@Component({
  selector   : 'app-userblock',
  templateUrl: './userblock.component.html',
  styleUrls  : ['./userblock.component.scss']
})
export class UserblockComponent implements OnInit {
  private _bsModalRef: BsModalRef;
  private _user: IUserRecord;

  constructor(public userService: UserService,
              public userblockService: UserblockService) {
  }

  ngOnInit() {
    this.userService.getCurrentUser().subscribe((user: IUserRecord) => {
      this._user = user;
    });
  }

  login(): void {
    this.userService.login();
  }

  userBlockIsVisible(): boolean {
    return this.userblockService.getVisibility();
  }

}
