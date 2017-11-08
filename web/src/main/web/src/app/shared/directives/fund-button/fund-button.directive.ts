import {Directive, HostListener, Input} from '@angular/core';
import {Request} from "../../../core/requests/Request";
import {BsModalRef, BsModalService} from "ngx-bootstrap";
import {FundModalComponent} from "../../../components/fund-modal/fund-modal.component";

@Directive({
  selector: '[fnd-fund]'
})
export class FundButtonDirective {
  @Input() request: Request;

  private bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService) {
  }

  @HostListener('click', ['$event'])
  onClick($event){
    this.openFundModal();
  }

  public openFundModal() {
    this.bsModalRef = this.modalService.show(FundModalComponent);
    console.log(this.request);

    this.bsModalRef.content.request = this.request;
  }
}
