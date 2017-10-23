import {ChangeDetectionStrategy, ChangeDetectorRef, Component, NgZone, OnInit} from "@angular/core";
import {Http} from "@angular/http";
import {Request} from "app/core/requests/request";
import {RequestsService} from "app/core/requests/requests.service";

@Component({
  selector: 'app-request-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {

  public requests: Array<Request> = [];
  private _ngZone: NgZone = new NgZone(false);

  constructor(public http: Http, private requestsService: RequestsService) {
  }

  async ngOnInit() {
    this.requests = await this.requestsService.getAll();
  }

  // angular2-datatable
  public sortByWordLength = (a: any) => {
    return a.name.length;
  };

  public fundRequest(request: Request) {
    console.log(request);
    /*
     this.requests = _.filter(this.requests, (elem) => elem != fundRequest);
     console.log('Remove: ', item.email);
     */
  }

  public onCellClick(data: any): any {
    console.log(data);
  }
}
