import { Injectable } from '@angular/core';

declare var $: any;

@Injectable()
export class SettingsService {

  public app: any;
  public layout: any;
  public status: any;

  constructor() {

    // App Settings
    // -----------------------------------
    this.app = {
      name       : 'FundRequest',
      description: 'Funding and rewarding open source development',
      year       : ((new Date()).getFullYear())
    };

    this.status = {
      openedFromChrome: false
    };

    // Layout Settings
    // -----------------------------------
    this.layout = {
      isFixed        : true,
      isCollapsed    : false,
      isBoxed        : false,
      isRTL          : false,
      horizontal     : false,
      isFloat        : false,
      asideHover     : false,
      theme          : null,
      asideScrollbar : false,
      isCollapsedText: false,
      useFullLayout  : false,
      hiddenFooter   : false,
      offsidebarOpen : false,
      asideToggled   : false,
      viewAnimation  : 'ng-fadeInUp'
    };

  }

}
