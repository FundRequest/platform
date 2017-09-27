import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';

import { ColorsService } from '../../../shared/colors/colors.service';

@Component({
    selector: 'app-dashboardv2',
    templateUrl: './dashboardv2.component.html',
    styleUrls: ['./dashboardv2.component.scss']
})
export class Dashboardv2Component implements OnInit {

    sparkOptions1 = {
        barColor: this.colors.byName('info'),
        height: 60,
        barWidth: 10,
        barSpacing: 6,
        chartRangeMin: 0
    };

    sparkOptions2 = {
        type: 'line',
        height: 60,
        width: '80%',
        lineWidth: 2,
        lineColor: this.colors.byName('purple'),
        chartRangeMin: 0,
        spotColor: '#888',
        minSpotColor: this.colors.byName('purple'),
        maxSpotColor: this.colors.byName('purple'),
        fillColor: '',
        highlightLineColor: '#fff',
        spotRadius: 3,
        resize: true
    };

    barStackedData: any;
    barStackedOptions = {
        series: {
            stack: true,
            bars: {
                align: 'center',
                lineWidth: 0,
                show: true,
                barWidth: 0.6,
                fill: 0.9
            }
        },
        grid: {
            borderColor: '#eee',
            borderWidth: 1,
            hoverable: true,
            backgroundColor: '#fcfcfc'
        },
        tooltip: true,
        tooltipOpts: {
            content: (label, x, y) => { return x + ' : ' + y; }
        },
        xaxis: {
            tickColor: '#fcfcfc',
            mode: 'categories'
        },
        yaxis: {
            min: 0,
            max: 200, // optional: use it for a clear represetation
            // position: ($rootScope.app.layout.isRTL ? 'right' : 'left'),
            tickColor: '#eee'
        },
        shadowSize: 0
    };

    splineData: any;
    splineOptions = {
        series: {
            lines: {
                show: false
            },
            points: {
                show: true,
                radius: 4
            },
            splines: {
                show: true,
                tension: 0.4,
                lineWidth: 1,
                fill: 0.5
            }
        },
        grid: {
            borderColor: '#eee',
            borderWidth: 1,
            hoverable: true,
            backgroundColor: '#fcfcfc'
        },
        tooltip: true,
        tooltipOpts: {
            content: (label, x, y) => { return x + ' : ' + y; }
        },
        xaxis: {
            tickColor: '#fcfcfc',
            mode: 'categories'
        },
        yaxis: {
            min: 0,
            max: 150, // optional: use it for a clear represetation
            tickColor: '#eee',
            // position: ($rootScope.app.layout.isRTL ? 'right' : 'left'),
            tickFormatter: (v) => {
                return v/* + ' visitors'*/;
            }
        },
        shadowSize: 0
    };


    easyPiePercent1 = 60;
    easyPiePercent2 = 30;
    easyPiePercent3 = 50;
    easyPiePercent4 = 75;

    pieOptions1 = {
        animate: {
            duration: 800,
            enabled: true
        },
        barColor: this.colors.byName('info'),
        trackColor: '#edf2f6',
        scaleColor: false,
        lineWidth: 2,
        lineCap: 'round',
        size: 130
    };
    pieOptions2 = {
        animate: {
            duration: 800,
            enabled: true
        },
        barColor: this.colors.byName('pink'),
        trackColor: '#edf2f6',
        scaleColor: false,
        lineWidth: 2,
        lineCap: 'round',
        size: 130
    };
    pieOptions3 = {
        animate: {
            duration: 800,
            enabled: true
        },
        barColor: this.colors.byName('purple'),
        trackColor: '#edf2f6',
        scaleColor: false,
        lineWidth: 2,
        lineCap: 'round',
        size: 130
    };
    pieOptions4 = {
        animate: {
            duration: 800,
            enabled: true
        },
        barColor: this.colors.byName('warning'),
        trackColor: '#edf2f6',
        scaleColor: false,
        lineWidth: 2,
        lineCap: 'round',
        size: 130
    };


    constructor(public colors: ColorsService, public http: Http) {
        http.get('assets/server/chart/barstackedv2.json').map(data => data.json()).subscribe(data => this.barStackedData = data);
        http.get('assets/server/chart/splinev2.json').map(data => data.json()).subscribe(data => this.splineData = data);
    }

    ngOnInit() {
    }

}
