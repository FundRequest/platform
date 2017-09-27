import { Component, OnInit } from '@angular/core';

import { ColorsService } from '../../../shared/colors/colors.service';

@Component({
    selector: 'app-widgets',
    templateUrl: './widgets.component.html',
    styleUrls: ['./widgets.component.scss']
})
export class WidgetsComponent implements OnInit {

    lat: number = 33.790807;
    lng: number = -117.835734;
    zoom: number = 14;
    scrollwheel = false;

    sparkOptions1 = {
        barColor: this.colors.byName('primary'),
        height: 20,
        barWidth: 3,
        barSpacing: 2
    };

    sparkOptions2 = {
        type: 'line',
        height: 80,
        width: '100%',
        lineWidth: 2,
        lineColor: '#dddddd',
        spotColor: '#bbbbbb',
        fillColor: '',
        highlightLineColor: '#fff',
        spotRadius: 3,
        resize: true
    };

    sparkOptions3 = {
        barColor: '#fff',
        height: 50,
        barWidth: 6,
        barSpacing: 6
    };

    sparkOptions4 = {
        barColor: this.colors.byName('primary'),
        height: 30,
        barWidth: 6,
        barSpacing: 6
    };

    constructor(public colors: ColorsService) { }

    ngOnInit() {
    }

}
