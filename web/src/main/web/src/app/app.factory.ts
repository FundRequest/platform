import { Injectable } from '@angular/core';
import { ContractsService } from './services/contracts/contracts.service';
import { SettingsService } from './core/settings/settings.service';

@Injectable()
export class AppFactory {
  constructor(private _cs: ContractsService, private _settings: SettingsService) {
  }

  public load(): Promise<boolean> {
    /*let _settings = this._settings;
     let _cs = this._cs;
     let _errorMessage = this._errorMessage;
     window.addEventListener('message', function (event) {
     if (event.origin == 'chrome-extension://bfphadjhpoonlgljmnpgjpnpjabbjeki') {
     console.log('data', event.data);
     switch (event.data.action) {
     case 'initialize':
     _settings.iframe = !!event.data.iframe;
     break;
     case 'fund':
     _cs.fundRequest(event.data.platform, event.data.platformId, event.data.url, event.data.value).then(function(result) {
     if(result == '-') {
     window.parent.postMessage({detail: {action: 'funded', id: event.data.id, success: false, errorMessage: _errorMessage}}, '*');
     } else {
     window.parent.postMessage({detail: {action: 'funded', id: event.data.id, success: true, tx: result}}, '*');
     }
     });
     }
     }
     });*/
    return this._cs.init().then(() => true).catch(() =>  false);
  }
}
