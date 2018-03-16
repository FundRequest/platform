import {Component, HostBinding, HostListener, OnInit} from '@angular/core';
import {ContractsService} from './services/contracts/contracts.service';
import {NotificationService} from './services/notification/notification.service';
import {SettingsService} from './core/settings/settings.service';
import {NotificationStreamService} from './services/sse/notification-stream.service';

declare let $: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  @HostBinding('class.layout-fixed')
  public get isFixed() { return this.settings.layout.isFixed; };

  @HostBinding('class.aside-collapsed')
  public get isCollapsed() { return this.settings.layout.isCollapsed; };

  @HostBinding('class.layout-boxed')
  public get isBoxed() { return this.settings.layout.isBoxed; };

  @HostBinding('class.layout-fs')
  public get useFullLayout() { return this.settings.layout.useFullLayout; };

  @HostBinding('class.hidden-footer')
  public get hiddenFooter() { return this.settings.layout.hiddenFooter; };

  @HostBinding('class.layout-h')
  public get horizontal() { return this.settings.layout.horizontal; };

  @HostBinding('class.aside-float')
  public get isFloat() { return this.settings.layout.isFloat; };

  @HostBinding('class.offsidebar-open')
  public get offsidebarOpen() { return this.settings.layout.offsidebarOpen; };

  @HostBinding('class.aside-toggled')
  public get asideToggled() { return this.settings.layout.asideToggled; };

  @HostBinding('class.aside-collapsed-text')
  public get isCollapsedText() { return this.settings.layout.isCollapsedText; };

  @HostListener('document:chrome.from.extension.fnd.opened')
  public openedFromChromeExtension() {
    this.settings.status.openedFromChrome = true;
  }

  constructor(public settings: SettingsService, private _nss: NotificationStreamService, private _cs: ContractsService, private _ns: NotificationService) { }

  ngOnInit() {
  }
}
