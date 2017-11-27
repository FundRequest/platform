import { Component, HostBinding, OnInit } from '@angular/core';
declare var $: any;

import { ContractsService } from './services/contracts/contracts.service';
import { NotificationService } from './services/notification/notification.service';
import { SettingsService } from './core/settings/settings.service';
import { NotificationStreamService } from './services/sse/notification-stream.service';

@Component({
  selector   : 'app-root',
  templateUrl: './app.component.html',
  styleUrls  : ['./app.component.scss']
})
export class AppComponent implements OnInit {

  @HostBinding('class.layout-fixed') get isFixed() { return this.settings.layout.isFixed; };

  @HostBinding('class.aside-collapsed') get isCollapsed() { return this.settings.layout.isCollapsed; };

  @HostBinding('class.layout-boxed') get isBoxed() { return this.settings.layout.isBoxed; };

  @HostBinding('class.layout-fs') get useFullLayout() { return this.settings.layout.useFullLayout; };

  @HostBinding('class.hidden-footer') get hiddenFooter() { return this.settings.layout.hiddenFooter; };

  @HostBinding('class.layout-h') get horizontal() { return this.settings.layout.horizontal; };

  @HostBinding('class.aside-float') get isFloat() { return this.settings.layout.isFloat; };

  @HostBinding('class.offsidebar-open') get offsidebarOpen() { return this.settings.layout.offsidebarOpen; };

  @HostBinding('class.aside-toggled') get asideToggled() { return this.settings.layout.asideToggled; };

  @HostBinding('class.aside-collapsed-text') get isCollapsedText() { return this.settings.layout.isCollapsedText; };

  constructor(public settings: SettingsService, private _nss: NotificationStreamService, private _cs: ContractsService, private _ns: NotificationService) { }

  ngOnInit() {
    this._cs.init();
    $(document).on('click', '[href="#"]', e => e.preventDefault());
  }
}
