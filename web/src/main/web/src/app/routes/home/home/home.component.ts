import { Component } from '@angular/core';

import { RequestService } from '../../../services/request/request.service';
import { RequestsStats } from '../../../core/requests/RequestsStats';

@Component({
  selector   : 'app-home',
  templateUrl: './home.component.html',
  styleUrls  : ['./home.component.scss']
})
export class HomeComponent {

  private _statistics: Promise<RequestsStats>;

  constructor(private requestService: RequestService) {
  }

  get statistics(): Promise<RequestsStats> {
    if (!this._statistics) {
      this._statistics = (async () => await this.requestService.getStatistics() as RequestsStats)();
    }
    return this._statistics;
  }
}
