import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-votelinks',
    templateUrl: './votelinks.component.html',
    styleUrls: ['./votelinks.component.scss']
})
export class VotelinksComponent implements OnInit {

    vCount1 = 10;
    vCount2 = 20;
    vCount3 = 27;
    vCount4 = 10;
    vCount5 = 5;
    vCount6 = 10;
    vCount7 = -1;
    vCount8 = 300;

    constructor() { }

    ngOnInit() { }

}
