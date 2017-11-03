import {Component} from '@angular/core';
import {Request} from "../../core/requests/Request";

import {BsModalRef} from 'ngx-bootstrap/modal/modal-options.class';
import {ContractsService} from "../../core/contracts/contracts.service";
import {UserService} from "../../core/user/user.service";

@Component({
  selector: 'modal-content',
  templateUrl: './fund-modal.component.html',
  styleUrls: ['./fund-modal.component.scss']
})
export class FundModalComponent {

  public request: Request;
  public fundAmount: number;
  public allowance: number;

  constructor(public bsModalRef: BsModalRef,
              private contractsService: ContractsService,
              private userService: UserService) {
    userService.getAllowance().then(allowance => this.allowance = Number.parseFloat(allowance));
  }

  public async fund() {
    console.log(this.fundAmount, this.allowance, this.fundAmount > this.allowance);
    if(this.fundAmount > this.allowance) {
      await this.userService.setAllowance(this.fundAmount - this.allowance);
    }
    this.request = await this.contractsService.fundRequest(this.request, this.fundAmount);
    this.bsModalRef.hide();
    // TODO save to database
    // await this.requestsService.update(request);
  }
}
