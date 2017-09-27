import { Component, OnInit } from '@angular/core';
import { Http } from '@angular/http';

import { ColorsService } from '../../../shared/colors/colors.service';

@Component({
    selector: 'app-dashboardv3',
    templateUrl: './dashboardv3.component.html',
    styleUrls: ['./dashboardv3.component.scss']
})
export class Dashboardv3Component implements OnInit {

    sparkOptions1: any;
    sparkOptions2: any;
    sparkOptions3: any;
    sparkOptions4: any;

    sparkOptions5 = {
        barColor: this.colors.byName('info'),
        height: 30,
        barWidth: '5',
        barSpacing: '2'
    };

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

    mapName: string;
    seriesData: any;
    markersData: any;
    mapOptions: any;

    defaultColors: any = {
        markerColor: '#23b7e5',      // the marker points
        bgColor: 'transparent',      // the background
        scaleColors: ['#878c9a'],    // the color of the region in the serie
        regionFill: '#bbbec6'       // the base region color
    };

    constructor(public colors: ColorsService, public http: Http) {

        // Sparkline
        this.sparkOptions1 = this.buildSparkOptionsBlock('info');
        this.sparkOptions2 = this.buildSparkOptionsBlock('purple');
        this.sparkOptions3 = this.buildSparkOptionsBlock('info');
        this.sparkOptions4 = this.buildSparkOptionsBlock('purple');

        // Flot CHart
        http.get('assets/server/chart/splinev3.json').map(data => data.json()).subscribe(data => this.splineData = data);

        // Vector Map
        this.mapName = 'world_mill_en';

        this.mapOptions = {
            markerColor: this.defaultColors.markerColor,
            bgColor: this.defaultColors.bgColor,
            scale: 1,
            scaleColors: this.defaultColors.scaleColors,
            regionFill: this.defaultColors.regionFill
        };

        this.seriesData = {
            'CA': 11100,   // Canada
            'DE': 2510,    // Germany
            'FR': 3710,    // France
            'AU': 5710,    // Australia
            'GB': 8310,    // Great Britain
            'RU': 9310,    // Russia
            'BR': 6610,    // Brazil
            'IN': 7810,    // India
            'CN': 4310,    // China
            'US': 839,     // USA
            'SA': 410      // Saudi Arabia
        };

        this.markersData = [
            { latLng: [41.90, 12.45], name: 'Vatican City' },
            { latLng: [43.73, 7.41], name: 'Monaco' },
            { latLng: [-0.52, 166.93], name: 'Nauru' },
            { latLng: [-8.51, 179.21], name: 'Tuvalu' },
            { latLng: [7.11, 171.06], name: 'Marshall Islands' },
            { latLng: [17.3, -62.73], name: 'Saint Kitts and Nevis' },
            { latLng: [3.2, 73.22], name: 'Maldives' },
            { latLng: [35.88, 14.5], name: 'Malta' },
            { latLng: [41.0, -71.06], name: 'New England' },
            { latLng: [12.05, -61.75], name: 'Grenada' },
            { latLng: [13.16, -59.55], name: 'Barbados' },
            { latLng: [17.11, -61.85], name: 'Antigua and Barbuda' },
            { latLng: [-4.61, 55.45], name: 'Seychelles' },
            { latLng: [7.35, 134.46], name: 'Palau' },
            { latLng: [42.5, 1.51], name: 'Andorra' }
        ];

    }

    // The four top block options only changes its color
    // use a common function to generate optiosn per color
    buildSparkOptionsBlock(color) {
        return {
            type: 'line',
            width: '100%',
            height: 75,
            lineColor: this.colors.byName(color),
            chartRangeMin: 0,
            fillColor: this.colors.byName(color),
            spotColor: this.colors.byName(color),
            minSpotColor: this.colors.byName(color),
            maxSpotColor: this.colors.byName(color),
            highlightSpotColor: this.colors.byName(color),
            highlightLineColor: this.colors.byName(color),
            resize: true
        };
    }

    ngOnInit() {
    }

}
