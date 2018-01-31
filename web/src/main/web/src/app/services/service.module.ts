import {NgModule, Optional, SkipSelf} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';

import {ContractsService} from './contracts/contracts.service';
import {KeycloakService} from './keycloak/keycloak.service';
import {NotificationService} from './notification/notification.service';
import {RequestService} from './request/request.service';
import {throwIfAlreadyLoaded} from '../core/module-import-guard';
import {ToasterService} from 'angular2-toaster';
import {UserService} from './user/user.service';
import {NotificationStreamService} from './sse/notification-stream.service';

@NgModule({
  imports     : [
    HttpClientModule
  ],
  providers   : [
    ContractsService,
    NotificationService,
    RequestService,
    ToasterService,
    UserService,
    KeycloakService,
    NotificationStreamService
  ],
  declarations: [],
  exports     : []
})

export class ServiceModule {
  constructor(@Optional() @SkipSelf() parentModule: ServiceModule) {
    throwIfAlreadyLoaded(parentModule, 'ServiceModule');
  }
}
