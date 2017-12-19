import {Component, OnInit} from '@angular/core';

import {IUserRecord} from '../../redux/user.models';
import {UserService} from '../../services/user/user.service';
import {ContractsService} from '../../services/contracts/contracts.service';
import {BsModalRef} from 'ngx-bootstrap/modal/bs-modal-ref.service';

@Component({
  selector   : 'approve-modal-content',
  templateUrl: './approve-modal.component.html',
  styleUrls  : ['./approve-modal.component.scss']
})
export class ApproveModalComponent implements OnInit {

  private _user: IUserRecord;
  public approveAmount: number = 0;

  constructor(public bsModalRef: BsModalRef,
              private _cs: ContractsService,
              private _us: UserService) {

  }

  ngOnInit() {
    this._us.getCurrentUser().subscribe((user: IUserRecord) => {
      this._user = user;
    });
  }

  public approve() {
    this._cs.setUserAllowance(this.approveAmount);
    this.bsModalRef.hide();
  }
}
