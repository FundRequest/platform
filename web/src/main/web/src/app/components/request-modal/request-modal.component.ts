import { Component, OnDestroy, OnInit } from '@angular/core';
import { BsModalRef } from 'ngx-bootstrap';
import { RequestService } from '../../services/request/request.service';
import { IRequestList } from '../../redux/requests.models';
import { Subscription } from 'rxjs/Subscription';
import { Router } from '@angular/router';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Issue } from './issue';
import { CustomValidators } from '../../custom-validators/custom-validators';

@Component({
  selector   : 'fnd-request-modal',
  templateUrl: './request-modal.component.html',
  styleUrls  : ['./request-modal.component.scss']
})
export class RequestModalComponent implements OnInit, OnDestroy {
  private _requests: IRequestList;
  private _subscription: Subscription;

  public title: string = 'Add Request';
  public requestForm: FormGroup;

  public issue: Issue = new Issue;

  constructor(public bsModalRef: BsModalRef, private _router: Router, private _rs: RequestService) {
  }

  ngOnInit(): void {
    this._subscription = this._rs.requests$.subscribe(result => this._requests = result);

    this.requestForm = new FormGroup({
      link        : new FormControl(this.issue.link, [
        Validators.required,
        Validators.pattern(/https:\/\/github.com\/FundRequest\/area51\/issues\/[0-9]+/),
        CustomValidators.requestExists(this._requests),
      ]),
      technologies: new FormControl(this.issue.technologies),
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
    for(let tech in this.technologies.value) {
      technologies.push(this.technologies.value[tech].value);
    }
    this._rs.addRequest(this.link.value.trim(), technologies);
    this.bsModalRef.hide();
  }

  get link() { return this.requestForm.get('link'); }

  get technologies() { return this.requestForm.get('technologies'); }
}
