import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DataTableModule } from 'angular2-datatable';
import { Ng2TableModule } from 'ng2-table/ng2-table';
import { AgGridModule } from 'ag-grid-angular/main';

import { SharedModule } from '../../shared/shared.module';
import { StandardComponent } from './standard/standard.component';
import { ExtendedComponent } from './extended/extended.component';
import { DatatableComponent } from './datatable/datatable.component';
import { AngulargridComponent } from './angulargrid/angulargrid.component';

const routes: Routes = [
    { path: 'standard', component: StandardComponent },
    { path: 'extended', component: ExtendedComponent },
    { path: 'datatable', component: DatatableComponent },
    { path: 'aggrid', component: AngulargridComponent }
];

@NgModule({
    imports: [
        SharedModule,
        RouterModule.forChild(routes),
        DataTableModule,
        Ng2TableModule,
        AgGridModule.withComponents([AngulargridComponent])
    ],
    declarations: [
        StandardComponent,
        ExtendedComponent,
        DatatableComponent,
        AngulargridComponent
    ],
    exports: [
        RouterModule
    ]
})
export class TablesModule { }
