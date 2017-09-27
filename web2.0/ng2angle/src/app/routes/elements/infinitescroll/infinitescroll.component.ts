import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-infinitescroll',
    templateUrl: './infinitescroll.component.html',
    styleUrls: ['./infinitescroll.component.scss']
})
export class InfinitescrollComponent implements OnInit {

    array = [];
    sum = 100;

    array2 = [];
    sum2 = 100;

    constructor() {
        for (let i = 0; i < this.sum; ++i) {
            this.array.push(i);
        }
        for (let i = 0; i < this.sum2; ++i) {
            this.array2.push(i);
        }
    }

    onScrollDown() {
        console.log('scrolled!!');

        // add another 20 items
        const start = this.sum;
        this.sum += 50;
        for (let i = start; i < this.sum; ++i) {
            this.array.push(i);
        }
    }

    onScrollDown2() {
        console.log('scrolled 2!!');

        // add another 20 items
        const start = this.sum2;
        this.sum2 += 50;
        for (let i = start; i < this.sum2; ++i) {
            this.array2.push(i);
        }
    }

    onScrollUp() {
        console.log('scrolled up!!');
    }

    ngOnInit() {
    }

}
