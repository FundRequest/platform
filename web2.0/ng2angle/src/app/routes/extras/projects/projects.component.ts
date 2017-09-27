import { Component, OnInit } from '@angular/core';

import { ColorsService } from '../../../shared/colors/colors.service';

@Component({
    selector: 'app-projects',
    templateUrl: './projects.component.html',
    styleUrls: ['./projects.component.scss']
})
export class ProjectsComponent implements OnInit {

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

    constructor(public colors: ColorsService) { }

    ngOnInit() {
    }

}
