import { Component, OnInit } from '@angular/core';
import { RequestsStats } from '../../core/requests/RequestsStats';
import { RequestService } from '../../services/request/request.service';

@Component({
  selector   : 'fnd-statistics',
  templateUrl: './statistics.component.html',
  styleUrls  : ['./statistics.component.scss']
})
export class StatisticsComponent {

  private _statistics: Promise<RequestsStats>;

  constructor(private requestService: RequestService) {
  }

  get statistics(): Promise<RequestsStats> {
    if (!this._statistics) {
      this._statistics = (async () => await this.requestService.getStatistics())();
    }
    return this._statistics;
  }
}


