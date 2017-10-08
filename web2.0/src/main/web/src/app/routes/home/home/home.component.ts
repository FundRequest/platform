import {Component, OnInit} from "@angular/core";

import {ColorsService} from "../../../shared/colors/colors.service";
import {RequestsService} from "../../../core/requests/requests.service";

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

    options = {
       /* animate: {
            duration: 800,
            enabled: true
        },
        barColor: this.colors.byName('info'),
        trackColor: 'rgba(200,200,200,0.4)',
        scaleColor: false,
        lineWidth: 10,
        lineCap: 'round',
        size: 145*/
    };


    constructor(public colors: ColorsService, public requestsService: RequestsService) {
      // get data from rest api
      console.log(requestsService.requests);
    }

    ngOnInit() { }

}
