import {Component} from '@angular/core';

import {BsModalRef} from 'ngx-bootstrap/modal/modal-options.class';
import {IRequestRecord} from "../../redux/requests.models";
import {IUserRecord} from "../../redux/user.models";
import {RequestService} from "../../services/request/request.service";
import {UserService} from "../../services/user/user.service";

@Component({
  selector: 'modal-content',
  templateUrl: './fund-modal.component.html',
  styleUrls: ['./fund-modal.component.scss']
})
export class FundModalComponent {

  public request: IRequestRecord;
  public user: IUserRecord;
  public fundAmount: number;
  public allowance: number = 0;
  public balance: number;

  constructor(public bsModalRef: BsModalRef,
              private requestService: RequestService,
              private userService: UserService) {
    this.userService.getCurrentUser().subscribe((user: IUserRecord) => {
      this.allowance = user.allowance;
      this.balance = user.balance;
    });
  }

  public async fund() {
    if(this.fundAmount > this.allowance) {
      await this.userService.setAllowance(this.fundAmount - this.allowance);
    }
    this.requestService.fundRequest(this.request, this.fundAmount);
    this.bsModalRef.hide();
  }
}
