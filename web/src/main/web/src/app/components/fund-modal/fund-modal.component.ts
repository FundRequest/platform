import {Component} from '@angular/core';
import {Request} from "../../core/requests/Request";

import {BsModalRef} from 'ngx-bootstrap/modal/modal-options.class';
import {ContractService} from "../../core/contracts/contracts.service";

@Component({
  selector: 'modal-content',
  templateUrl: './fund-modal.component.html',
  styleUrls: ['./fund-modal.component.scss']
})
export class FundModalComponent {

  public request: Request;
  public fundAmount: number;

  constructor(public bsModalRef: BsModalRef, private contractService: ContractService) {
  }

  public async fund() {
    this.request = await this.contractService.fundRequest(this.request, this.fundAmount);
    this.bsModalRef.hide();
    // TODO save to database
    // await this.requestsService.update(request);
  }
}
