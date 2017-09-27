import { Component, OnInit, OnDestroy } from '@angular/core';
import { GridOptions } from 'ag-grid/main';
import { Http } from '@angular/http';
import * as _ from 'lodash';
declare var $: any;

@Component({
    selector: 'app-angulargrid',
    templateUrl: './angulargrid.component.html',
    styleUrls: ['./angulargrid.component.scss']
})
export class AngulargridComponent implements OnInit, OnDestroy {

    resizeEvent = 'resize.ag-grid';
    $win = $(window);

    gridOptions: GridOptions;
    gridOptions1: GridOptions;
    gridOptions2: GridOptions;

    // Basic example
    columnDefs = [
        {
            headerName: 'Athlete',
            field: 'athlete',
            width: 150
        }, {
            headerName: 'Age',
            field: 'age',
            width: 90
        }, {
            headerName: 'Country',
            field: 'country',
            width: 120
        }, {
            headerName: 'Year',
            field: 'year',
            width: 90
        }, {
            headerName: 'Date',
            field: 'date',
            width: 110
        }, {
            headerName: 'Sport',
            field: 'sport',
            width: 110
        }, {
            headerName: 'Gold',
            field: 'gold',
            width: 100
        }, {
            headerName: 'Silver',
            field: 'silver',
            width: 100
        }, {
            headerName: 'Bronze',
            field: 'bronze',
            width: 100
        }, {
            headerName: 'Total',
            field: 'total',
            width: 100
        }];

    // Filter Example
    irishAthletes = ['John Joe Nevin', 'Katie Taylor', 'Paddy Barnes', 'Kenny Egan', 'Darren Sutherland', 'Margaret Thatcher', 'Tony Blair', 'Ronald Regan', 'Barack Obama'];

    columnDefsFilter = [{
        headerName: 'Athlete',
        field: 'athlete',
        width: 150,
        filter: 'set',
        filterParams: {
            cellHeight: 20,
            values: this.irishAthletes
        }
    }, {
        headerName: 'Age',
        field: 'age',
        width: 90,
        filter: 'number'
    }, {
        headerName: 'Country',
        field: 'country',
        width: 120
    }, {
        headerName: 'Year',
        field: 'year',
        width: 90
    }, {
        headerName: 'Date',
        field: 'date',
        width: 110
    }, {
        headerName: 'Sport',
        field: 'sport',
        width: 110
    }, {
        headerName: 'Gold',
        field: 'gold',
        width: 100,
        filter: 'number'
    }, {
        headerName: 'Silver',
        field: 'silver',
        width: 100,
        filter: 'number'
    }, {
        headerName: 'Bronze',
        field: 'bronze',
        width: 100,
        filter: 'number'
    }, {
        headerName: 'Total',
        field: 'total',
        width: 100,
        filter: 'number'
    }];

    // Pinned example
    columnDefsPinned: any;

    constructor(http: Http) {
        // Basic example
        this.gridOptions = <GridOptions>{
            columnDefs: this.columnDefs,
            rowData: null,
        };


        // Filter example
        this.gridOptions1 = <GridOptions>{
            columnDefs: this.columnDefsFilter,
            rowData: null,
            enableFilter: true,
        };

        // Pinned Example
        this.columnDefsPinned = _.cloneDeep(this.columnDefs);
        this.columnDefsPinned[0].pinned = 'left';
        this.gridOptions2 = <GridOptions>{
            columnDefs: this.columnDefsPinned,
            rowData: null,
            pinnedColumnCount: 2,
        };

        // Load from JSON
        http.get('assets/server/ag-owinners.json')
            .subscribe((data) => {

                this.gridOptions.api.setRowData(data.json());
                this.gridOptions.api.sizeColumnsToFit();

                this.gridOptions1.api.setRowData(data.json());
                this.gridOptions1.api.sizeColumnsToFit();

                this.gridOptions2.api.setRowData(data.json());
                this.gridOptions2.api.sizeColumnsToFit();

            });
    }

    onQuickFilterChanged($event) {
        this.gridOptions1.api.setQuickFilter($event.target.value);
    }

    ngOnInit() { }

    gridReady(params) {
        params.api.sizeColumnsToFit();
        this.$win.on(this.resizeEvent, () => {
            setTimeout(() => { params.api.sizeColumnsToFit(); });
        });
    }

    gridReady1(params) {
        params.api.sizeColumnsToFit();
        this.$win.on(this.resizeEvent, () => {
            setTimeout(() => { params.api.sizeColumnsToFit(); });
        });
    }

    gridReady2(params) {
        params.api.sizeColumnsToFit();
        this.$win.on(this.resizeEvent, () => {
            setTimeout(() => { params.api.sizeColumnsToFit(); });
        });
    }

    ngOnDestroy() {
        this.$win.off(this.resizeEvent);
    }

}
