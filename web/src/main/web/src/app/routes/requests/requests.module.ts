import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {Ng2TableModule} from "ng2-table/ng2-table";

import {SharedModule} from "../../shared/shared.module";
import {OverviewComponent} from "./overview/overview.component";
import {DetailComponent} from "./detail/detail.component";

const routes: Routes = [
  {path: '', component: OverviewComponent},
  {path: 'detail/id', component: DetailComponent}
];

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(routes),
    Ng2TableModule,
  ],
  declarations: [
    DetailComponent,
    OverviewComponent
  ],
  exports: [
    RouterModule
  ]
})
export class RequestsModule {}
