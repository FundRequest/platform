import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {ChartsModule as Ng2ChartsModule} from "ng2-charts/ng2-charts";

import {SharedModule} from "../../shared/shared.module";
import {ChartjsComponent} from "./chartjs/chartjs.component";
import {RadialComponent} from "./radial/radial.component";

const routes: Routes = [
    { path: 'radial', component: RadialComponent },
    { path: 'chartjs', component: ChartjsComponent }
];

@NgModule({
    imports: [
        SharedModule,
        RouterModule.forChild(routes),
        Ng2ChartsModule
    ],
    declarations: [
        RadialComponent,
        ChartjsComponent
    ],
    exports: [
        RouterModule
    ]
})
export class ChartsModule { }
