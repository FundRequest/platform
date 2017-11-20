import {NgModule, Optional, SkipSelf} from "@angular/core";
import {HttpClientModule} from "@angular/common/http";
import {throwIfAlreadyLoaded} from "../core/module-import-guard";
import {ToasterService} from "angular2-toaster";
import {RequestService} from "./request/request.service";
import {UserService} from "./user/user.service";
import {ContractsService} from "./contracts/contracts.service";
import {NotificationService} from "./notification/notification.service";
import {KeycloakService} from "./keycloak/keycloak.service";

@NgModule({
  imports: [
    HttpClientModule
  ],
  providers: [
    ContractsService,
    NotificationService,
    RequestService,
    ToasterService,
    UserService,
    KeycloakService
  ],
  declarations: [],
  exports: []
})

export class ServiceModule {
  constructor(@Optional() @SkipSelf() parentModule: ServiceModule) {
    throwIfAlreadyLoaded(parentModule, 'ServiceModule');
  }
}
