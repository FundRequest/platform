import { NgModule } from '@angular/core';

import { LayoutComponent } from './layout.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { HeaderComponent } from './header/header.component';
import { NavsearchComponent } from './header/navsearch/navsearch.component';
import { OffsidebarComponent } from './offsidebar/offsidebar.component';
import { UserblockComponent } from './userblock/userblock.component';
import { UserblockService } from './userblock/userblock.service';
import { FooterComponent } from './footer/footer.component';

import { SharedModule } from '../shared/shared.module';
import { NetworkStatusComponent } from './network-status/network-status.component';

@NgModule({
  imports     : [
    SharedModule
  ],
  providers   : [
    UserblockService
  ],
  declarations: [
    LayoutComponent,
    SidebarComponent,
    UserblockComponent,
    HeaderComponent,
    NavsearchComponent,
    OffsidebarComponent,
    FooterComponent,
    NetworkStatusComponent
  ],
  exports     : [
    LayoutComponent,
    SidebarComponent,
    UserblockComponent,
    HeaderComponent,
    NavsearchComponent,
    OffsidebarComponent,
    FooterComponent
  ]
})
export class LayoutModule {
}
