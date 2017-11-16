import {Component, Input, OnInit} from '@angular/core';
import {RequestsStats} from "../../core/requests/RequestsStats";

@Component({
  selector: 'fnd-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnInit {

  @Input() statistics: RequestsStats;

  pieOptions = {
    animate: {
      duration: 800,
      enabled: true
    },
    barColor: '#fff',
    trackColor: 'rgba(93,156,236,0.4)',
    scaleColor: false,
    lineWidth: 5,
    lineCap: 'round',
    size: 55
  };

  constructor() {
  }

  ngOnInit() {
  }

  public get percentageFunded(): number {
    return this.statistics.requestsFunded == 0 ? 0 : (this.statistics.requestsFunded / this.statistics.numberOfRequests) * 100;
  }
}


