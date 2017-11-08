import {Component, OnInit} from "@angular/core";
import {Request} from "app/core/requests/Request";
import {ContractsService} from "app/core/contracts/contracts.service";
import {IRequestList, IRequestRecord, RequestService} from "app/services/request/request.service";
import {Observable} from "rxjs/Observable";

@Component({
  selector: 'app-request-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {

  requests$: Observable<IRequestList>;
  requestsRows: any;
  request: IRequestRecord;

  constructor(public requestService: RequestService, public contractsService: ContractsService) {
    console.log(this.requests$);
    this.requests$ = this.requestService.requests;
    this.requests$.subscribe(requests => this.requestsRows = requests);
    console.log(this.requests$);

    /*
    this.requests$.map(
      (list: IRequestList) => list && list.get(0)
    ).subscribe(request => this.request = request);
    console.log(this.requests$);*/
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
