import {Directive, HostListener, Input} from '@angular/core';
import {Request} from "../../../core/requests/Request";
import {BsModalRef, BsModalService} from "ngx-bootstrap";
import {RequestModalComponent} from "../../../components/request-modal/request-modal.component";
import {Observable} from "rxjs/Observable";
import {IRequestList, RequestService} from "../../../services/request/request.service";
import {IState} from "../../../redux/store";

@Directive({
  selector: '[fnd-add-request]'
})
export class AddRequestDirective {

  private bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService) {
  }

  @HostListener('click', ['$event'])
  onClick($event) {
    this.openModal();
  };

  public openModal() {
    this.bsModalRef = this.modalService.show(RequestModalComponent);
  }
}
