import {Component, OnInit} from "@angular/core";
import {Request} from "app/core/requests/Request";
import {ContractsService} from "app/core/contracts/contracts.service";
import {IRequestList, RequestService} from "app/services/request/request.service";

@Component({
  selector: 'app-request-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {

  requests: IRequestList;

  constructor(public requestService: RequestService, public contractsService: ContractsService) {
    this.requestService.requests.subscribe(list => {
      this.requests = list;
    });
  }

  ngOnInit(): void {
  }

  /*
  private async getRequests(): Promise<Request[]> {
    this.requests = await this.requestsService.getAll();
    for (let i = 0; i < this.requests.length; i++) {
      this.contractsService.getRequestBalance(String(this.requests[i].id)).then(
        balance => this.requests[i].balance = balance
      );
    }
    return this.requests;
  }*/

  // angular2-datatable
  public sortByWordLength = (a: any) => {
    return a.name.length;
  };


  public async fundRequest(request: Request): Promise<void> {
    request = await this.contractsService.fundRequest(request, 1) as Request;

    // TODO save to database
    // await this.requestsService.update(request);
  }

  public onCellClick(data: any): any {
    console.log(data);
  }
}
