import {NgModule} from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import {WatchlinkComponent} from './watchlink/watchlink.component';
import {CommonModule} from '@angular/common';
import {StatisticsComponent} from './statistics/statistics.component';
import {EasypiechartDirective} from '../shared/directives/easypiechart/easypiechart.directive';
import {SharedModule} from '../shared/shared.module';
import {FundModalComponent} from './fund-modal/fund-modal.component';
import {RequestModalComponent} from './request-modal/request-modal.component';
import {TagInputModule} from 'ngx-chips';
import {NotificationStreamComponent} from './notification-stream/notification-stream.component';
import {MomentModule} from 'angular2-moment';
import {QRCodeModule} from 'angular2-qrcode';

@NgModule({
  imports        : [
    HttpClientModule,
    CommonModule,
    SharedModule,
    TagInputModule,
    MomentModule,
    QRCodeModule
],
  providers      : [],
  declarations   : [
    WatchlinkComponent,
    StatisticsComponent,
    FundModalComponent,
    RequestModalComponent,
    NotificationStreamComponent
  ],
  exports        : [
    WatchlinkComponent,
    StatisticsComponent,
    FundModalComponent,
    RequestModalComponent,
    EasypiechartDirective,
    NotificationStreamComponent
  ],
  entryComponents: [
    FundModalComponent,
    RequestModalComponent
  ]
})
export class ComponentsModule {

}
