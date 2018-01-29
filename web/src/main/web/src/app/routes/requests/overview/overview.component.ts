import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {RequestService} from '../../../services/request/request.service';
import {IRequestList} from '../../../redux/requests.models';
import {IUserRecord} from '../../../redux/user.models';
import {UserService} from '../../../services/user/user.service';
import {Subscription} from 'rxjs/Subscription';
import {Utils} from '../../../shared/utils';

@Component({
  selector: 'app-request-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {
  public user: IUserRecord;
  public subscription: Subscription;
  public requests$: Observable<IRequestList>;
  public requestsRows: Array<any> = [];
  private _rowsOnPageSet: Array<number> = null;

  public requestFilter: any = {
    issueInformation: {
      title: ''
    }
  };

  constructor(private _us: UserService,
    private _rs: RequestService) {

  }

  async ngOnInit() {
    this._us.currentUser$.subscribe(user => this.user = user);
    this.requests$ = this._rs.requests$;
    this.subscription = this.requests$.subscribe(
      requests => {
        this.requestsRows = requests.toArray();

        if (typeof this.requestsRows !== 'undefined') {
          for (let i in this.requestsRows) {
            this.requestsRows[i].fundInfo.balanceNumber = Utils.fromWeiRounded(this.requestsRows[i].fundInfo.balance);
          }
          let size = this.requestsRows.length;

          if (size > 50) {
            this._rowsOnPageSet = [5, 10, 20, 50];
          } else if (size > 20) {
            this._rowsOnPageSet = [5, 10, 20];
          } else if (size > 10) {
            this._rowsOnPageSet = [5, 10];
          }
        }
      }
    );
  }

  public get rowsOnPageSet(): Array<number> {
    return this._rowsOnPageSet;
  }


  public onCellClick(data: any): any {
  }
}
