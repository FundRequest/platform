import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';

import { ColorsService } from '../../../shared/colors/colors.service';

@Component({
    selector: 'app-dashboardv1',
    templateUrl: './dashboardv1.component.html',
    styleUrls: ['./dashboardv1.component.scss']
})
export class Dashboardv1Component implements OnInit {

    easyPiePercent: number = 70;
    pieOptions = {
        animate: {
            duration: 800,
            enabled: true
        },
        barColor: this.colors.byName('info'),
        trackColor: 'rgba(200,200,200,0.4)',
        scaleColor: false,
        lineWidth: 10,
        lineCap: 'round',
        size: 145
    };

    sparkOptions1 = {
        barColor: this.colors.byName('info'),
        height: 30,
        barWidth: '5',
        barSpacing: '2'
    };

    sparkOptions2 = {
        type: 'line',
        height: 80,
        width: '100%',
        lineWidth: 2,
        lineColor: this.colors.byName('purple'),
        spotColor: '#888',
        minSpotColor: this.colors.byName('purple'),
        maxSpotColor: this.colors.byName('purple'),
        fillColor: '',
        highlightLineColor: '#fff',
        spotRadius: 3,
        resize: true
    };

    splineHeight = 280;
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
            // position: ($scope.app.layout.isRTL ? 'right' : 'left'),
            tickFormatter: (v) => {
                return v/* + ' visitors'*/;
            }
        },
        shadowSize: 0
    };

    constructor(public colors: ColorsService, public http: Http) {
        http.get('assets/server/chart/spline.json').map(data => data.json()).subscribe(data => this.splineData = data);
    }

    ngOnInit() { }

    colorByName(name) {
        return this.colors.byName(name);
    }

}
