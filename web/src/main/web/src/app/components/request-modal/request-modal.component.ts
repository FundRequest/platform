import {Component, OnDestroy, OnInit} from '@angular/core';
import {BsModalRef} from 'ngx-bootstrap';
import {RequestService} from '../../services/request/request.service';
import {IRequestList, IRequestRecord} from '../../redux/requests.models';
import {Subscription} from 'rxjs/Subscription';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Issue} from './issue';
import {Utils} from '../../shared/utils';
import {UserService} from "../../services/user/user.service";
import {IUserRecord} from "../../redux/user.models";

@Component({
  selector   : 'fnd-request-modal',
  templateUrl: './request-modal.component.html',
  styleUrls  : ['./request-modal.component.scss']
})
export class RequestModalComponent implements OnInit, OnDestroy {
  private _requests: IRequestList;
  private _subscription: Subscription;

  public title: string = 'Fund other request';
  public requestForm: FormGroup;
  public user: IUserRecord;
  public fundAmount: number;
  public allowance: number;
  public balance: number;


  public issue: Issue = new Issue;

  constructor(public bsModalRef: BsModalRef, private _router: Router, private _rs: RequestService, private userService: UserService) {

  }

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe((user: IUserRecord) => {
      this.user = user;
      this.balance = Utils.fromWeiRounded(user.balance);
      this.allowance = Utils.fromWeiRounded(user.allowance);
    });
    this._subscription = this._rs.requests$.subscribe(result => this._requests = result);

    this.requestForm = new FormGroup({
      link        : new FormControl(this.issue.link, [
        Validators.required,
        Validators.pattern(/^https:\/\/github.com\/FundRequest\/area51\/issues\/[0-9]+$/),
      ]),
      'fund-amount': new FormControl(this.fundAmount)
    });
  }

  public gotoRequest(id: number | string): void {
    this._router.navigate([`/requests/${id}`]);
    this.bsModalRef.hide();
  }

  ngOnDestroy() {
    this._subscription.unsubscribe();
  }

  public addRequest() {
    let technologies = [];
    this._rs.addRequest(this.link.value.trim(), this.fundAmount);
    this.bsModalRef.hide();
  }

  get link() { return this.requestForm.get('link'); }

  public requestExists():boolean {
    let checkRequests = this._requests.filter((request: IRequestRecord) => request.issueInformation.link == this.link.value.trim());
    return checkRequests.count() > 0;
  }

}
