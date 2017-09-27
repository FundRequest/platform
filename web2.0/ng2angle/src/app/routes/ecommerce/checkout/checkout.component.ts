import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-checkout',
    templateUrl: './checkout.component.html',
    styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {

    step1 = true;
    step2 = false;
    step3 = false;
    step4 = false;
    step5 = false;
    step6 = false;

    constructor() { }

    ngOnInit() { }

}
