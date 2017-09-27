import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'app-buttons',
    templateUrl: './buttons.component.html',
    styleUrls: ['./buttons.component.scss']
})
export class ButtonsComponent implements OnInit {
    // buttons
    public singleModel: boolean = true;
    public radioModel: string = 'Middle';
    public checkModel: any = { left: false, middle: true, right: false };

    // pagination/pager
    public totalItems: number = 64;
    public currentPage: number = 4;

    public maxSize: number = 5;
    public bigTotalItems: number = 175;
    public bigCurrentPage: number = 1;

    public setPage(pageNo: number): void {
        this.currentPage = pageNo;
    };
    smallnumPages;
    numPages;

    public pageChanged(event: any): void {
        console.log('Page changed to: ' + event.page);
        console.log('Number items per page: ' + event.itemsPerPage);
    };

    constructor() { }

    ngOnInit() {
    }

}
