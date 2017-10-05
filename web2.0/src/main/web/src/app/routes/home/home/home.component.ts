import {Component, OnInit} from "@angular/core";
import {Http} from "@angular/http";

import {ColorsService} from "../../../shared/colors/colors.service";

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


    constructor(public colors: ColorsService, public http: Http) {
      // get data from rest api
        http.get('rest-entry-point/').map(data => data.json()).subscribe(data => this.options = data);
    }

    ngOnInit() { }

}
