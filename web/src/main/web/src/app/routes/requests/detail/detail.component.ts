import {Component, OnInit} from "@angular/core";
import {RequestsService} from "../../../core/requests/requests.service";
import {Request} from "../../../core/requests/Request";
import {ActivatedRoute} from "@angular/router";
import {ContractsService} from "app/core/contracts/contracts.service";
import {Subscription} from "rxjs/Subscription";

@Component({
  selector: 'fnd-request-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {

  private subRoute: Subscription;
  public id;
  public request: Request;


  constructor(private route: ActivatedRoute,
              private requestsService: RequestsService,
              private contractsService: ContractsService) {
  }

  ngOnInit(): void {
    this.subRoute = this.route.params.subscribe(params => {
      this.getRequest(+params['id']);
    });
  }

  async getRequest(id: number) {
    this.request = await this.requestsService.get(id);
    this.request.balance = await this.contractsService.getRequestBalance(String(id));
    console.log(this.request);
  }
}
