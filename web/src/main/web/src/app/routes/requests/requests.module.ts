import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {Ng2TableModule} from "ng2-table/ng2-table";

import {SharedModule} from "../../shared/shared.module";
import {OverviewComponent} from "./overview/overview.component";
import {DetailComponent} from "./detail/detail.component";
import {ComponentsModule} from "../../components/components.module";
import {DataTableModule} from "angular2-datatable";
import {FundModalComponent} from "./fund-modal/fund-modal.component";
import {FundButtonComponent} from "./fund-button/fund-button.component";

const routes: Routes = [
  {path: '', component: OverviewComponent},
  {path: ':id', component: DetailComponent}
];

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(routes),
    Ng2TableModule,
    ComponentsModule,
    DataTableModule
  ],
  declarations: [
    DetailComponent,
    OverviewComponent,
    FundModalComponent,
    FundButtonComponent
  ],
  exports: [
    RouterModule
  ],
  entryComponents: [
    FundModalComponent
  ]
})
export class RequestsModule {}
