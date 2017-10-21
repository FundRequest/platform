import {NgModule, Optional, SkipSelf} from "@angular/core";
import {HttpClientModule} from "@angular/common/http";
import {throwIfAlreadyLoaded} from "../core/module-import-guard";
import {WatchlinkComponent} from "./watchlink/watchlink.component";
import {CommonModule} from "@angular/common";

@NgModule({
  imports: [
    HttpClientModule,
    CommonModule
  ],
  providers: [
  ],
  declarations: [
    WatchlinkComponent
  ],
  exports: [
    WatchlinkComponent
  ]
})
export class ComponentsModule {

}
