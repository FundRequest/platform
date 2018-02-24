import {Component, OnDestroy, OnInit} from '@angular/core';
import {BsModalRef} from 'ngx-bootstrap';
import {RequestService} from '../../services/request/request.service';
import {IRequestList, IRequestRecord} from '../../redux/requests.models';
import {Subscription} from 'rxjs/Subscription';
import {Router} from '@angular/router';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {Utils} from '../../shared/utils';
import {AccountWeb3Service} from '../../services/accountWeb3/account-web3.service';
import {IAccountWeb3Record} from '../../redux/accountWeb3.models';

@Component({
  selector: 'fnd-request-modal',
  templateUrl: './request-modal.component.html',
  styleUrls: ['./request-modal.component.scss']
})
export class RequestModalComponent implements OnInit, OnDestroy {

  private _requests: IRequestList;
  private _subscriptionAccountWeb3: Subscription;
  private _subscriptionRequests: Subscription;

  public title: string = 'Fund other request';
  public requestForm: FormGroup;
  public acountWeb3: IAccountWeb3Record;
  public balance: number;

  constructor(
    public bsModalRef: BsModalRef,
    private _router: Router,
    private _rs: RequestService,
    private _aw3s: AccountWeb3Service) {
  }

  ngOnInit(): void {
    this._subscriptionAccountWeb3 = this._aw3s.currentAccountWeb3$.subscribe((acountWeb3: IAccountWeb3Record) => {
      this.acountWeb3 = acountWeb3;
      this.balance = Utils.fromWeiRounded(acountWeb3.balance);
    });
    this._subscriptionRequests = this._rs.requests$.subscribe(result => this._requests = result);

    this.requestForm = new FormGroup({
      linkControl: new FormControl('', [
        Validators.required,
        Validators.pattern(/^https:\/\/github.com\/FundRequest\/area51\/issues\/[0-9]+$/)
      ]),
      fundAmountControl: new FormControl('', [
        Validators.required,
        Validators.min(0.01),
        Validators.max(this.balance)
      ])
    });
  }

  public gotoRequest(id: number | string): void {
    this._router.navigate([`/requests/${id}`]);
    this.bsModalRef.hide();
  }

  public addRequest() {
    this._rs.addRequest(this.linkValue, this.fundAmountValue);
    this.bsModalRef.hide();
    this.requestForm.reset();
  }

  public get linkControl(): FormControl {
    return this.requestForm.get('linkControl') as FormControl;
  }

  public get fundAmountControl(): FormControl {
    return this.requestForm.get('fundAmountControl') as FormControl;
  }

  public get linkValue(): string {
    let linkValue: string = this.linkControl.value;
    return linkValue ? linkValue.trim() : '';
  }

  public get fundAmountValue(): number {
    return this.fundAmountControl.value as number;
  }

  public get platform(): string {
    return Utils.getPlatformFromUrl(this.linkValue);
  }

  public get platformId(): string {
    return Utils.getPlatformIdFromUrl(this.linkValue);
  }

  public get requestExists(): boolean {
    let checkRequests = this._requests.filter((request: IRequestRecord) =>
      (request.issueInformation.platform == this.platform && request.issueInformation.platformId == this.platformId)
    );
    return checkRequests.count() > 0;
  }

  ngOnDestroy() {
    this._subscriptionAccountWeb3.unsubscribe();
    this._subscriptionRequests.unsubscribe();
  }

}
