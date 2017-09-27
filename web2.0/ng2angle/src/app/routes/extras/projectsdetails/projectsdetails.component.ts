import { Component, OnInit } from '@angular/core';

import { ColorsService } from '../../../shared/colors/colors.service';

@Component({
    selector: 'app-projectsdetails',
    templateUrl: './projectsdetails.component.html',
    styleUrls: ['./projectsdetails.component.scss']
})
export class ProjectsdetailsComponent implements OnInit {

    sparkOptionsInfo = {
        type: 'pie',
        sliceColors: [this.colors.byName('gray-lighter'), this.colors.byName('info')],
        height: 24
    };

    sparkOptionsWarning = {
        type: 'pie',
        sliceColors: [this.colors.byName('gray-lighter'), this.colors.byName('warning')],
        height: 24
    };

    sparkOptionsSuccess = {
        type: 'pie',
        sliceColors: [this.colors.byName('gray-lighter'), this.colors.byName('success')],
        height: 24
    };

    sparkOptionsActivity = {
        type: 'line',
        height: 100,
        width: '100%',
        lineWidth: 3,
        lineColor:this.colors.byName('info'),
        chartRangeMin: 0,
        spotColor: '#888',
        minSpotColor:this.colors.byName('info'),
        maxSpotColor:this.colors.byName('info'),
        fillColor: '#e5f2fa',
        highlightLineColor: '#fff',
        spotRadius: 5,
        resize: 'true'
    }

    constructor(public colors: ColorsService) { }

    ngOnInit() {
    }

}
