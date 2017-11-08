import {Component} from '@angular/core';
import {BsModalRef} from "ngx-bootstrap";
import {RequestService} from "../../services/request/request.service";

@Component({
  selector: 'fnd-request-modal',
  templateUrl: './request-modal.component.html',
  styleUrls: ['./request-modal.component.scss']
})
export class RequestModalComponent {
  public title: string;
  public issueLink: string;
  public technologies: any = [];

  constructor(public bsModalRef: BsModalRef, private requestService: RequestService) {
    this.title = 'Add Request';
  }

  public addRequest() {
    let technologies = [];
    for(let i = 0; i < this.technologies.length; i++) {
      technologies.push(this.technologies[i].value);
    }
    this.requestService.addRequest(this.issueLink, technologies);
    this.bsModalRef.hide();
  }
}
