import {Injectable, OnInit} from '@angular/core';

import {HttpClient} from '@angular/common/http';
import {Settings} from './settings.model';

declare var $: any;

@Injectable()
export class SettingsService implements OnInit {

  public app: any;
  public layout: any;
  public status: any;

  private _settings: Settings = null;

  constructor(private _http: HttpClient) {
    // App Settings
    // -----------------------------------
    this.app = {
      name: 'FundRequest',
      description: 'Funding and rewarding open source development',
      year: ((new Date()).getFullYear())
    };

    this.status = {
      openedFromChrome: false
    };

    // Layout Settings
    // -----------------------------------
    this.layout = {
      isFixed: true,
      isCollapsed: false,
      isBoxed: false,
      isRTL: false,
      horizontal: false,
      isFloat: false,
      asideHover: false,
      theme: null,
      asideScrollbar: false,
      isCollapsedText: false,
      useFullLayout: false,
      hiddenFooter: false,
      offsidebarOpen: false,
      asideToggled: false,
      viewAnimation: 'ng-fadeInUp'
    };
  }

  async ngOnInit() {
    this._settings = await this.getSettings();
  }

  public async getSettings(): Promise<Settings> {
    if (this._settings == null) {
      let envSettings: any = await this._http.get('/env').toPromise();
      let applicationConfig = envSettings[`applicationConfig: [classpath:/application${envSettings.profiles[0] == 'dev' ? '-dev' : ''}.properties]`];
      this._settings = new Settings();
      this._settings.fundRequestContractAddress = applicationConfig['io.fundrequest.contract.fund-request.address'];
      this._settings.tokenContractAddress = applicationConfig['io.fundrequest.contract.token.address'];
      this._settings.appVersion = applicationConfig['io.fundrequest.app-version'];
    }

    return this._settings;
  }
}
