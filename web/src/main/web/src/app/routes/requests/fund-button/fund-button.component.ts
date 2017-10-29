import {Component, Input, OnInit} from '@angular/core';
import {Request} from "../../../core/requests/Request";
import {FundModalComponent} from "../fund-modal/fund-modal.component";
import {BsModalRef, BsModalService} from "ngx-bootstrap";

@Component({
  selector: 'fnd-fund-button',
  templateUrl: './fund-button.component.html',
  styleUrls: ['./fund-button.component.scss']
})
export class FundButtonComponent implements OnInit {
  @Input() request: Request;

  private bsModalRef: BsModalRef;

  constructor(private modalService: BsModalService) {
  }

  ngOnInit() {
  }

  public openFundModal() {
    this.bsModalRef = this.modalService.show(FundModalComponent);
    console.log(this.request);

    this.bsModalRef.content.request = this.request;
  }
}
