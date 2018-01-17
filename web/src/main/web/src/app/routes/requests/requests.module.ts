import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Ng2TableModule} from 'ng2-table/ng2-table';

import {SharedModule} from '../../shared/shared.module';
import {OverviewComponent} from './overview/overview.component';
import {DetailComponent} from './detail/detail.component';
import {FundComponent} from "./fund/fund.component";
import {ComponentsModule} from '../../components/components.module';
import {DataTableModule} from 'angular2-datatable';
import {QRCodeModule} from 'angular2-qrcode';

const routes: Routes = [
  {path: '', component: OverviewComponent},
  {path: 'fund', component: FundComponent},
  {path: ':id', component: DetailComponent}
];

@NgModule({
  imports     : [
    SharedModule,
    RouterModule.forChild(routes),
    Ng2TableModule,
    ComponentsModule,
    DataTableModule,
    QRCodeModule,
  ],
  declarations: [
    DetailComponent,
    OverviewComponent,
    FundComponent
  ],
  exports     : [
    RouterModule
  ],
})
export class RequestsModule {
}
