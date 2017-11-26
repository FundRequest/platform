import { Component } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { RequestService } from '../../../services/request/request.service';
import { IRequestList, IRequestRecord } from '../../../redux/requests.models';
import { IUserRecord } from '../../../redux/user.models';
import { UserService } from '../../../services/user/user.service';

@Component({
  selector   : 'app-request-overview',
  templateUrl: './overview.component.html',
  styleUrls  : ['./overview.component.scss']
})
export class OverviewComponent {
  public user: IUserRecord;
  public requests$: Observable<IRequestList>;
  public requestsRows: Array<IRequestRecord> = [];

  constructor(private _us: UserService,
              private _rs: RequestService) {
    this._us.getCurrentUser().subscribe(user => this.user = user);
    this.requests$ = this._rs.requests$;
    this.requests$.map(requests => requests.toArray()).subscribe(
      requests => this.requestsRows = requests
    );
  }

  public onCellClick(data: any): any {
  }
}
