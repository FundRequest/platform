import {NgModule} from "@angular/core";
import {HttpClientModule} from "@angular/common/http";
import {WatchlinkComponent} from "./watchlink/watchlink.component";
import {CommonModule} from "@angular/common";
import {StatisticsComponent} from './statistics/statistics.component';
import {EasypiechartDirective} from "../shared/directives/easypiechart/easypiechart.directive";
import {SharedModule} from "../shared/shared.module";
import {FundModalComponent} from "./fund-modal/fund-modal.component";
import {RequestModalComponent} from './request-modal/request-modal.component';
import {ApproveModalComponent} from "./approve-modal/approve-modal.component";

@NgModule({
  imports: [
    HttpClientModule,
    CommonModule,
    SharedModule
  ],
  providers: [],
  declarations: [
    WatchlinkComponent,
    StatisticsComponent,
    FundModalComponent,
    ApproveModalComponent,
    RequestModalComponent
  ],
  exports: [
    WatchlinkComponent,
    StatisticsComponent,
    FundModalComponent,
    ApproveModalComponent,
    RequestModalComponent,
    EasypiechartDirective
  ],
  entryComponents: [
    FundModalComponent,
    ApproveModalComponent,
    RequestModalComponent
  ]
})
export class ComponentsModule {

}
