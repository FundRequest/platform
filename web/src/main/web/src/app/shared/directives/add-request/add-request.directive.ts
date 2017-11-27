import { Directive, HostListener } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { RequestModalComponent } from '../../../components/request-modal/request-modal.component';

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
