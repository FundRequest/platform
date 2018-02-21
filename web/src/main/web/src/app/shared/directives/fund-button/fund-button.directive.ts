import { Directive, HostListener, Input } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { FundModalComponent } from '../../../components/fund-modal/fund-modal.component';
import { IRequestRecord } from '../../../redux/requests.models';

@Directive({
  selector: '[fnd-fund]'
})
export class FundButtonDirective {
  @Input() request: IRequestRecord;

  private bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService) {
  }

  @HostListener('click', ['$event'])
  onClick($event) {
    this.openFundModal();
  }

  public openFundModal() {
    this.bsModalRef = this.modalService.show(FundModalComponent, {class: 'modal-dialog--with-tabs'});
    this.bsModalRef.content.request = this.request;
  }
}
