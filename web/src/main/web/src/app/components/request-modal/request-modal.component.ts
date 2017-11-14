import {Component, OnDestroy, OnInit} from '@angular/core';
import {BsModalRef} from "ngx-bootstrap";
import {RequestService} from "../../services/request/request.service";
import {IRequestList, IRequestRecord} from "../../redux/requests.models";
import {Observable} from "rxjs/Observable";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'fnd-request-modal',
  templateUrl: './request-modal.component.html',
  styleUrls: ['./request-modal.component.scss']
})
export class RequestModalComponent implements OnInit, OnDestroy {
  private _requests: IRequestList;
  private _subscription: Subscription;
  public title: string;
  public issueLink: string;
  public technologies: any = [];

  constructor(public bsModalRef: BsModalRef, private _rs: RequestService) {
  }

  private issueExists(): boolean {
    if (this.issueLink != null && this._requests) {
      return this._requests.filter((request: IRequestRecord) => request.issueInformation.link == this.issueLink.trim()).count() > 0;
    }
  }

  ngOnInit() {
    this.title = 'Add Request';
    this._subscription = this._rs.requests$.subscribe(result => this._requests = result);
  }

  ngOnDestroy() {
    this._subscription.unsubscribe();
  }

  public addRequest() {
    let technologies = [];
    for (let i = 0; i < this.technologies.length; i++) {
      technologies.push(this.technologies[i].value);
    }
    this._rs.addRequest(this.issueLink.trim(), technologies);
    this.bsModalRef.hide();
  }
}
