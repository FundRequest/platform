import { Component, OnInit } from '@angular/core';
import { Observable } from "rxjs/Observable";
import { Http } from '@angular/http';
declare var $: any;

@Component({
    selector: 'app-flot',
    templateUrl: './flot.component.html',
    styleUrls: ['./flot.component.scss']
})
export class FlotComponent implements OnInit {

    // AREA
    // -----------------------------------
    areaData: any;
    areaOptions = {
        series: {
            lines: {
                show: true,
                fill: 0.8
            },
            points: {
                show: true,
                radius: 4
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
            content: function(label, x, y) { return x + ' : ' + y; }
        },
        xaxis: {
            tickColor: '#fcfcfc',
            mode: 'categories'
        },
        yaxis: {
            min: 0,
            tickColor: '#eee',
            // position: ($scope.app.layout.isRTL ? 'right' : 'left'),
            tickFormatter: function(v) {
                return v + ' visitors';
            }
        },
        shadowSize: 0
    };

    // BAR
    // -----------------------------------
    barData: any;
    barOptions = {
        series: {
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
            content: function(label, x, y) { return x + ' : ' + y; }
        },
        xaxis: {
            tickColor: '#fcfcfc',
            mode: 'categories'
        },
        yaxis: {
            // position: ($scope.app.layout.isRTL ? 'right' : 'left'),
            tickColor: '#eee'
        },
        shadowSize: 0
    };

    // BAR STACKED
    // -----------------------------------
    barStackeData: any;
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
            content: function(label, x, y) { return x + ' : ' + y; }
        },
        xaxis: {
            tickColor: '#fcfcfc',
            mode: 'categories'
        },
        yaxis: {
            min: 0,
            max: 200, // optional: use it for a clear represetation
            // position: ($scope.app.layout.isRTL ? 'right' : 'left'),
            tickColor: '#eee'
        },
        shadowSize: 0
    };

    // LINE
    // -----------------------------------
    lineData: any;
    lineOptions = {
        series: {
            lines: {
                show: true,
                fill: 0.01
            },
            points: {
                show: true,
                radius: 4
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
            content: function(label, x, y) { return x + ' : ' + y; }
        },
        xaxis: {
            tickColor: '#eee',
            mode: 'categories'
        },
        yaxis: {
            // position: ($scope.app.layout.isRTL ? 'right' : 'left'),
            tickColor: '#eee'
        },
        shadowSize: 0
    };

    // PIE
    // -----------------------------------
    pieData = [{
        'label': 'jQuery',
        'color': '#4acab4',
        'data': 30
    }, {
            'label': 'CSS',
            'color': '#ffea88',
            'data': 40
        }, {
            'label': 'LESS',
            'color': '#ff8153',
            'data': 90
        }, {
            'label': 'SASS',
            'color': '#878bb6',
            'data': 75
        }, {
            'label': 'Jade',
            'color': '#b2d767',
            'data': 120
        }];
    pieOptions = {
        series: {
            pie: {
                show: true,
                innerRadius: 0,
                label: {
                    show: true,
                    radius: 0.8,
                    formatter: function(label, series) {
                        return '<div class="flot-pie-label">' +
                            // label + ' : ' +
                            Math.round(series.percent) +
                            '%</div>';
                    },
                    background: {
                        opacity: 0.8,
                        color: '#222'
                    }
                }
            }
        }
    };

    // DONUT
    // -----------------------------------
    donutData = [{
        'color': '#39C558',
        'data': 60,
        'label': 'Coffee'
    },
        {
            'color': '#00b4ff',
            'data': 90,
            'label': 'CSS'
        },
        {
            'color': '#FFBE41',
            'data': 50,
            'label': 'LESS'
        },
        {
            'color': '#ff3e43',
            'data': 80,
            'label': 'Jade'
        },
        {
            'color': '#937fc7',
            'data': 116,
            'label': 'AngularJS'
        }
    ];
    donutOptions = {
        series: {
            pie: {
                show: true,
                innerRadius: 0.5 // This makes the donut shape
            }
        }
    };

    // SPLINE
    // -----------------------------------
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
            content: function(label, x, y) { return x + ' : ' + y; }
        },
        xaxis: {
            tickColor: '#fcfcfc',
            mode: 'categories'
        },
        yaxis: {
            min: 0,
            max: 150, // optional: use it for a clear represetation
            tickColor: '#eee',
            // position: (app.layout.isRTL ? 'right' : 'left'),
            tickFormatter: function(v) {
                return v/* + ' visitors'*/;
            }
        },
        shadowSize: 0
    };

    // REALTIME
    // -----------------------------------
    rtAuxData = [];
    realTimeData: any;
    realTimeOptions = {
        series: {
            lines: { show: true, fill: true, fillColor: { colors: ['#a0e0f3', '#23b7e5'] } },
            shadowSize: 0 // Drawing is faster without shadows
        },
        grid: {
            show: false,
            borderWidth: 0,
            minBorderMargin: 20,
            labelMargin: 10
        },
        xaxis: {
            tickFormatter: function() {
                return '';
            }
        },
        yaxis: {
            min: 0,
            max: 110
        },
        legend: {
            show: true
        },
        colors: ['#23b7e5']
    };

    constructor(public http: Http) {
        this.getChartData('assets/server/chart/spline.json').subscribe(data => this.splineData = data);
        this.getChartData('assets/server/chart/bar.json').subscribe(data => this.barData = data);
        this.getChartData('assets/server/chart/barstacked.json').subscribe(data => this.barStackeData = data);
        this.getChartData('assets/server/chart/area.json').subscribe(data => this.areaData = data);
        this.getChartData('assets/server/chart/line.json').subscribe(data => this.lineData = data);

    }

    ngOnInit() {
        // Generate random data for realtime demo
        this.update();
    }

    getChartData(url): Observable<any> {
        return this.http.get(url).map(data => data.json());
    }

    change() {
        // Change a value
        this.splineData[0].color = '#ff0000';
        // Create a new object in order to provide a different instance
        // so the directive can detect the change using ngOnChanges
        this.splineData = $.extend(true, [], this.splineData);
    }

    ready($event) {
        // $event == { plot: PlotObject }
        console.log('Ready!');
    }

    // REALTIME demo
    // -----------------------------------
    getRandomData() {
        let totalPoints = 300;
        if (this.rtAuxData.length > 0) {
            this.rtAuxData = this.rtAuxData.slice(1);
        }
        // Do a random walk
        while (this.rtAuxData.length < totalPoints) {
            let prev = this.rtAuxData.length > 0 ? this.rtAuxData[this.rtAuxData.length - 1] : 50,
                y = prev + Math.random() * 10 - 5;
            if (y < 0) {
                y = 0;
            } else if (y > 100) {
                y = 100;
            }
            this.rtAuxData.push(y);
        }
        // Zip the generated y values with the x values
        let res = [];
        for (let i = 0; i < this.rtAuxData.length; ++i) {
            res.push([i, this.rtAuxData[i]]);
        }
        return [res];
    }

    update() {
        this.realTimeData = this.getRandomData();
        setTimeout(this.update.bind(this), 30);
    }

}
