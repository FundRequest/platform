import {Component} from "@angular/core";

import {UserblockService} from "./userblock.service";
import {UserService} from "../../../services/user/user.service";
import {IUserRecord} from "../../../redux/user.models";
import {BsModalRef, BsModalService} from "ngx-bootstrap";
import {ApproveModalComponent} from "../../../components/approve-modal/approve-modal.component";

@Component({
  selector: 'app-userblock',
  templateUrl: './userblock.component.html',
  styleUrls: ['./userblock.component.scss']
})
export class UserblockComponent {
  private _bsModalRef: BsModalRef;
  private _user: IUserRecord;

  constructor(public userService: UserService,
              public userblockService: UserblockService,
              private modalService: BsModalService) {
    userService.getCurrentUser().subscribe((user: IUserRecord) => {
      this._user = user;
    });
  }

  approveModal(): void {
    this._bsModalRef = this.modalService.show(ApproveModalComponent);
  }

  login(): void {
    this.userService.login();
  }

  userBlockIsVisible(): boolean {
    return this.userblockService.getVisibility();
  }
}
