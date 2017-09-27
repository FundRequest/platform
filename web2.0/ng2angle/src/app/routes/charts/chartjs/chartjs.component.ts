import { Component, OnInit } from '@angular/core';
import { ColorsService } from '../../../shared/colors/colors.service';

@Component({
    selector: 'app-chartjs',
    templateUrl: './chartjs.component.html',
    styleUrls: ['./chartjs.component.scss']
})
export class ChartjsComponent implements OnInit {


    // Line chart
    // -----------------------------------

    lineData = {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [
            {
                label: 'My First dataset',
                data: [this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor()]
            }, {
                label: 'My Second dataset',
                data: [this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor()]
            }]
    };

    lineColors = [
        {
            backgroundColor: 'rgba(114,102,186,0.2)',
            borderColor: 'rgba(114,102,186,1)',
            pointBackgroundColor: 'rgba(114,102,186,1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(114,102,186,1)'
        }, {
            backgroundColor: 'rgba(35,183,229,0.2)',
            borderColor: 'rgba(35,183,229,1)',
            pointBackgroundColor: 'rgba(35,183,229,1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(35,183,229,1)'
        }];

    lineOptions = {
        animation: false,
        responsive: true
    };


    // Bar chart
    // -----------------------------------

    barData = {
        labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July'],
        datasets: [
            {
                data: [this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor()]
            }, {
                data: [this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor(), this.rFactor()]
            }]
    };

    barColors = [
        {
            backgroundColor: this.colors.byName('info'),
            borderColor: this.colors.byName('info'),
            pointHoverBackgroundColor: this.colors.byName('info'),
            pointHoverBorderColor: this.colors.byName('info')
        }, {
            backgroundColor: this.colors.byName('primary'),
            borderColor: this.colors.byName('primary'),
            pointHoverBackgroundColor: this.colors.byName('primary'),
            pointHoverBorderColor: this.colors.byName('primary')
        }];

    barOptions = {
        scaleShowVerticalLines: false,
        responsive: true
    };


    //  Doughnut chart
    // -----------------------------------

    doughnutData = {
        labels: [
            'Purple',
            'Info',
            'Yellow'
        ],
        datasets: [{
            data: [30, 50, 20]
        }]
    };

    doughnutColors = [{
        borderColor: [
            this.colors.byName('purple'),
            this.colors.byName('info'),
            this.colors.byName('yellow')
        ],
        backgroundColor: [
            this.colors.byName('purple'),
            this.colors.byName('info'),
            this.colors.byName('yellow')
        ],
    }];

    doughnutOptions = {
        responsive: true
    };

    // Pie chart
    // -----------------------------------

    pieData = {
        labels: [
            'Purple',
            'Yellow',
            'Info'
        ],
        datasets: [{
            data: [30, 40, 30]
        }]
    };

    pieColors = [{
        borderColor: [
            this.colors.byName('info'),
            this.colors.byName('yellow'),
            this.colors.byName('purple')
        ],
        backgroundColor: [
            this.colors.byName('info'),
            this.colors.byName('yellow'),
            this.colors.byName('purple')
        ],
    }];

    pieOptions = {
        responsive: true
    };

    // Polar chart
    // -----------------------------------

    polarData = {
        datasets: [{
            data: [
                300,
                50,
                100,
                140
            ],
            label: 'My dataset' // for legend
        }],
        labels: [
            'Red',
            'Green',
            'Yellow',
            'Grey'
        ]
    };

    polarColors = [
        {
            backgroundColor: [
                this.colors.byName('pink'),
                this.colors.byName('purple'),
                this.colors.byName('pink'),
                this.colors.byName('purple')
            ],
            borderColor: [
                this.colors.byName('pink'),
                this.colors.byName('purple'),
                this.colors.byName('pink'),
                this.colors.byName('purple')
            ],
        }];

    polarOptions = {
        responsive: true
    };


    // Radar chart
    // -----------------------------------

    radarData = {
        labels: ['Eating', 'Drinking', 'Sleeping', 'Designing', 'Coding', 'Cycling', 'Running'],
        datasets: [
            {
                label: 'My First dataset',
                data: [65, 59, 90, 81, 56, 55, 40]
            }, {
                label: 'My Second dataset',
                data: [28, 48, 40, 19, 96, 27, 100]
            }]
    };

    radarColors = [
        {
            backgroundColor: 'rgba(114,102,186,0.2)',
            borderColor: 'rgba(114,102,186,1)',
            pointColor: 'rgba(114,102,186,1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(114,102,186,1)'
        }, {
            backgroundColor: 'rgba(151,187,205,0.2)',
            borderColor: 'rgba(151,187,205,1)',
            pointColor: 'rgba(151,187,205,1)',
            pointBorderColor: '#fff',
            pointHoverBackgroundColor: '#fff',
            pointHoverBorderColor: 'rgba(151,187,205,1)'
        }];

    radarOptions = {
        responsive: true
    };

    constructor(public colors: ColorsService) { }

    ngOnInit() {
    }

    // random values for demo
    rFactor() {
        return Math.round(Math.random() * 100);
    };

}
