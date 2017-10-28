import {NgModule, Optional, SkipSelf} from "@angular/core";
import {HttpClientModule} from "@angular/common/http";
import {throwIfAlreadyLoaded} from "../core/module-import-guard";
import {WatchlinkComponent} from "./watchlink/watchlink.component";
import {CommonModule} from "@angular/common";
import {StatisticsComponent} from './statistics/statistics.component';
import {EasypiechartDirective} from "../shared/directives/easypiechart/easypiechart.directive";
import {SharedModule} from "../shared/shared.module";

@NgModule({
  imports: [
    HttpClientModule,
    CommonModule,
    SharedModule,
  ],
  providers: [],
  declarations: [
    WatchlinkComponent,
    StatisticsComponent
  ],
  exports: [
    WatchlinkComponent,
    EasypiechartDirective,
    StatisticsComponent
  ]
})
export class ComponentsModule {

}
