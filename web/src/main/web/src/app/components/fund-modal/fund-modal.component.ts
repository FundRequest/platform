import {Component, OnInit} from '@angular/core';

import {FundRequestCommand, IRequestRecord} from '../../redux/requests.models';
import {RequestService} from '../../services/request/request.service';
import {Utils} from '../../shared/utils';
import {BsModalRef} from 'ngx-bootstrap/modal/bs-modal-ref.service';
import {AccountWeb3Service} from '../../services/accountWeb3/account-web3.service';
import {IAccountWeb3Record} from '../../redux/accountWeb3.models';

@Component({
  selector: 'fund-modal-content',
  templateUrl: './fund-modal.component.html',
  styleUrls: ['./fund-modal.component.scss']
})
export class FundModalComponent implements OnInit {

  public request: IRequestRecord;
  public accountWeb3: IAccountWeb3Record;
  public fundAmount: number;
  public balance: number;
  public qrValue: string = '';


  constructor(public bsModalRef: BsModalRef,
    private _rs: RequestService,
    private _aw3s: AccountWeb3Service) {
  }

  ngOnInit() {
    this._aw3s.currentAccountWeb3$.subscribe((accountWeb3: IAccountWeb3Record) => {
      this.accountWeb3 = accountWeb3;
      this.balance = Utils.fromWeiRounded(accountWeb3.balance);
    });
  }

  public hasEnoughFunds() {
    return this.balance >= this.fundAmount;
  }

  public canFund(): boolean {
    return this.fundAmount > 0;
  }

  public updateQr() {
    this._rs.requestQRValue(new FundRequestCommand(
      this.request.issueInformation.platform,
      this.request.issueInformation.platformId,
      this.fundAmount
    )).then(
      res => { // Success
        this.qrValue = res;
      },
      msg => { // Error
      }
    );
  }

  public fund() {
    this._rs.fundRequest(
      new FundRequestCommand(
        this.request.issueInformation.platform,
        this.request.issueInformation.platformId,
        this.fundAmount)
    );
    this.bsModalRef.hide();
  }
}
