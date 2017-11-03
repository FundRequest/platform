import {Component, Input} from '@angular/core';
import {BsModalRef} from "ngx-bootstrap";
import {IRequestList, RequestService} from "../../services/request/request.service";

@Component({
  selector: 'fnd-request-modal',
  templateUrl: './request-modal.component.html',
  styleUrls: ['./request-modal.component.scss']
})
export class RequestModalComponent {
  public title: string;
  public issueLink: string;
  public technologies: string[] = [];

  constructor(public bsModalRef: BsModalRef, private requestService: RequestService) {
    this.title = 'Add Request';
    this.technologies.push('test');
    this.technologies.push('test2');
  }

  public addRequest() {
    this.requestService.addRequest(this.issueLink, this.technologies);
    this.bsModalRef.hide();
  }
}
