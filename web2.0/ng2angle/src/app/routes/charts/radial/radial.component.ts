import { Component, OnInit } from '@angular/core';
import { ColorsService } from '../../../shared/colors/colors.service';

@Component({
    selector: 'app-radial',
    templateUrl: './radial.component.html',
    styleUrls: ['./radial.component.scss']
})
export class RadialComponent implements OnInit {

    easyPiePercent1: number = 85;
    easyPiePercent2: number = 45;
    easyPiePercent3: number = 25;
    easyPiePercent4: number = 60;

    pieOptions1 = {
        animate: {
            duration: 800,
            enabled: true
        },
        barColor: this.colors.byName('success'),
        trackColor: false,
        scaleColor: false,
        lineWidth: 10,
        lineCap: 'circle'
    };

    pieOptions2 = {
        animate: {
            duration: 800,
            enabled: true
        },
        barColor: this.colors.byName('warning'),
        trackColor: false,
        scaleColor: false,
        lineWidth: 4,
        lineCap: 'circle'
    };

    pieOptions3 = {
        animate: {
            duration: 800,
            enabled: true
        },
        barColor: this.colors.byName('danger'),
        trackColor: false,
        scaleColor: this.colors.byName('gray'),
        lineWidth: 15,
        lineCap: 'circle'
    };

    pieOptions4 = {
        animate: {
            duration: 800,
            enabled: true
        },
        barColor: this.colors.byName('danger'),
        trackColor: this.colors.byName('yellow'),
        scaleColor: this.colors.byName('gray-dark'),
        lineWidth: 15,
        lineCap: 'circle'
    };


    constructor(public colors: ColorsService) { }

    ngOnInit() {
    }

    randomize() {
        this.easyPiePercent1 = this.random();
        this.easyPiePercent2 = this.random();
        this.easyPiePercent3 = this.random();
        this.easyPiePercent4 = this.random();
    }

    random() {
        return Math.floor((Math.random() * 100) + 1);
    }

}
