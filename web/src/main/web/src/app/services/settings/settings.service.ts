import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Settings} from './settings.model';

@Injectable()
export class SettingsService {

  private _settings: Settings = null;

  constructor(private _http: HttpClient) {
  }

  public async getSettings(): Promise<Settings> {
    if (this._settings == null) {
      let envSettings: any = await this._http.get('/env').toPromise();
      let applicationConfig = envSettings[`applicationConfig: [classpath:/application${envSettings.profiles[0] == 'dev' ? '-dev' : ''}.properties]`];
      this._settings = new Settings();
      this._settings.fundRequestContractAddress = applicationConfig['io.fundrequest.contract.fund-request.address'];
      this._settings.tokenContractAddress = applicationConfig['io.fundrequest.contract.token.address'];
    }

    return Promise.resolve(this._settings);
  }
}


