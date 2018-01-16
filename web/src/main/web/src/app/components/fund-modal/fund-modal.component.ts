import {Component, OnInit} from '@angular/core';

import {FundRequestCommand, IRequestRecord} from '../../redux/requests.models';
import {IUserRecord} from '../../redux/user.models';
import {RequestService} from '../../services/request/request.service';
import {UserService} from '../../services/user/user.service';
import {Utils} from '../../shared/utils';
import {BsModalRef} from 'ngx-bootstrap/modal/bs-modal-ref.service';

@Component({
  selector: 'fund-modal-content',
  templateUrl: './fund-modal.component.html',
  styleUrls: ['./fund-modal.component.scss']
})
export class FundModalComponent implements OnInit {

  public request: IRequestRecord;
  public user: IUserRecord;
  public fundAmount: number;
  public balance: number;

  constructor(public bsModalRef: BsModalRef,
              private requestService: RequestService,
              private userService: UserService) {
  }

  ngOnInit() {
    this.userService.getCurrentUser().subscribe((user: IUserRecord) => {
      this.user = user;
      this.balance = Utils.fromWeiRounded(user.balance);
    });
  }

  public fund() {
    this.requestService.fundRequest(
      new FundRequestCommand(
        this.request.issueInformation.platform,
        this.request.issueInformation.platformId,
        this.request.issueInformation.link,
        this.fundAmount)
    );
    this.bsModalRef.hide();
  }
}
