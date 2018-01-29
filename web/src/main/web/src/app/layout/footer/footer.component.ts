import {Component} from '@angular/core';
import {SettingsService} from '../../core/settings/settings.service';
import {Settings} from '../../core/settings/settings.model';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: '[app-footer]',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.scss']
})
export class FooterComponent {

  private _appVersion: string;

  constructor(public ss: SettingsService) {
    ss.getSettings().then((result: Settings) => {
      this._appVersion = result.appVersion;
    });
  }

  public get appVersion$(): Observable<string> {
    return Observable.of(this._appVersion);
  }
}
