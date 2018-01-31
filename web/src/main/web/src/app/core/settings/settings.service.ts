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
      let applicationConfig = this.buildApplicationConfig(envSettings);
      /*
       let envProd = envSettings['applicationConfig: [classpath:/application.properties]'];
       let envDev = envSettings['applicationConfig: [classpath:/application-dev.properties]'];
       let applicationConfig = Object.assign(envDev, envProd);
       */
      this._settings = new Settings();
      this._settings.fundRequestContractAddress = applicationConfig['io.fundrequest.contract.fund-request.address'];
      this._settings.tokenContractAddress = applicationConfig['io.fundrequest.contract.token.address'];
      this._settings.appVersion = applicationConfig['io.fundrequest.app-version'];
      this._settings.providerApi = 'https://ropsten.fundrequest.io/';
      this._settings.etherscan = 'https://ropsten.etherscan.io/';
    }

    return this._settings;
  }

  private buildApplicationConfig(envSettings: any) {
    let applicationConfig = {};
    for (let key in envSettings) {
      if (envSettings.hasOwnProperty(key)) {
        if (key.startsWith('applicationConfig')) {
          this.copyAppConfig(envSettings[key], applicationConfig);
        }
      }
    }
    return applicationConfig;
  }

  private copyAppConfig(foundAppConfig: any, applicationConfig: {}) {
    for (let prop in foundAppConfig) {
      if (foundAppConfig.hasOwnProperty(prop)) {
        applicationConfig[prop] = foundAppConfig[prop];
      }
    }
  }
}
