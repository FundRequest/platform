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

  user: IUserRecord;
  requests$: Observable<IRequestList>;
  requestsRows: Array<IRequestRecord> = [];

  constructor(private userServivce: UserService,
              private requestService: RequestService) {
    this.userServivce.getCurrentUser().subscribe(user => this.user = user);
    this.requests$ = this.requestService.requests$;
    this.requests$.map(requests => requests.toArray()).subscribe(
      requests => this.requestsRows = requests
    );
  }

  public onCellClick(data: any): any {
    console.log(data);
  }
}
