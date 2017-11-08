import {NgModule, Optional, SkipSelf} from "@angular/core";
import {RequestService} from "./request/request.service";
import {UserService} from "./user/user.service";
import {ContractsService} from "./contracts/contracts.service";
import {throwIfAlreadyLoaded} from "../core/module-import-guard";
import {HttpClientModule} from "@angular/common/http";

@NgModule({
  imports: [
    HttpClientModule
  ],
  providers: [
    RequestService,
    UserService,
    ContractsService
  ],
  declarations: [],
  exports: []
})

export class ServiceModule {
  constructor(@Optional() @SkipSelf() parentModule: ServiceModule) {
    throwIfAlreadyLoaded(parentModule, 'ServiceModule');
  }
}
