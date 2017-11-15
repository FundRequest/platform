import {Component} from '@angular/core';

import {BsModalRef} from 'ngx-bootstrap/modal/modal-options.class';
import {IUserRecord} from "../../redux/user.models";
import {UserService} from "../../services/user/user.service";
import {ContractsService} from "../../services/contracts/contracts.service";

@Component({
  selector: 'approve-modal-content',
  templateUrl: './approve-modal.component.html',
  styleUrls: ['./approve-modal.component.scss']
})
export class ApproveModalComponent {

  private _user: IUserRecord;
  private _approveAmount: number = 0;

  constructor(public bsModalRef: BsModalRef,
              private _cs: ContractsService,
              private _us: UserService) {
    this._us.getCurrentUser().subscribe((user: IUserRecord) => {
      this._user = user;
    });
  }

  public approve() {
    this._cs.setUserAllowance(this._approveAmount);
    this.bsModalRef.hide();
  }
}
