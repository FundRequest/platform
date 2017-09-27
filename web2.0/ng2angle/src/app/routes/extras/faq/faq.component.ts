import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-faq',
    templateUrl: './faq.component.html',
    styleUrls: ['./faq.component.scss']
})
export class FaqComponent implements OnInit {

    isAccOpen1 = false;
    isAccOpen2 = false;
    isAccOpen3 = false;
    isAccOpen4 = false;
    isAccOpen5 = false;
    isAccOpen6 = false;

    constructor() { }

    ngOnInit() { }

}
