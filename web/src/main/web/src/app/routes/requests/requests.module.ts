import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {Ng2TableModule} from "ng2-table/ng2-table";

import {SharedModule} from "../../shared/shared.module";
import {OverviewComponent} from "./overview/overview.component";

const routes: Routes = [
    { path: 'overview', component: OverviewComponent }
];

@NgModule({
    imports: [
        SharedModule,
        RouterModule.forChild(routes),
        Ng2TableModule,
    ],
    declarations: [
        OverviewComponent
    ],
    exports: [
        RouterModule
    ]
})
export class RequestsModule { }
