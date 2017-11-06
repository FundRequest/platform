import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {ContractsService} from "app/core/contracts/contracts.service";
import {Subscription} from "rxjs/Subscription";
import {IRequestRecord, RequestService} from "../../../services/request/request.service";

@Component({
  selector: 'fnd-request-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {

  private subRoute: Subscription;
  public request: IRequestRecord;

  constructor(private route: ActivatedRoute,
              private requestService: RequestService,
              private contractsService: ContractsService) {
  }

  ngOnInit(): void {
    this.subRoute = this.route.params.subscribe(params => {
      let id = +params['id'];
      this.requestService
        .requests.map(list => list.filter(request => request.id == id).first())
        .subscribe(request => {
          this.request = request;
        });
    });
  }
/*
  async getRequest(id: number) {
    this.request = await this.requestsService.get(id);
    this.request.balance = await this.contractsService.getRequestBalance(String(id));
    console.log(this.request);
  }*/
}
