import {Component, Input} from '@angular/core';
import {BsModalRef} from "ngx-bootstrap";
import {Request} from "../../core/requests/Request";
import {RequestsService} from "../../core/requests/requests.service";

@Component({
  selector: 'fnd-request-modal',
  templateUrl: './request-modal.component.html',
  styleUrls: ['./request-modal.component.scss']
})
export class RequestModalComponent {

  public requests: Request[];
  public title: string;
  public issueLink: string;
  public technologies: string[] = [];

  constructor(public bsModalRef: BsModalRef, private requestService: RequestsService) {
    this.title = 'Add Request';
    this.technologies.push('test');
    this.technologies.push('test2');
  }

  public async addRequest() {
    let request = await this.requestService.add(this.issueLink, this.technologies) as Request;
    console.log(request);
    this.requests.push(request);
    this.bsModalRef.hide();
  }
}
