import { Injectable } from '@angular/core';
import { BodyOutputType, Toast, ToasterService } from 'angular2-toaster';
import { NotificationType } from './notificationType';
import { SettingsService } from '../../core/settings/settings.service';

@Injectable()
export class NotificationService {

  private _toastSuccess: Toast = {
    type          : 'success',
    bodyOutputType: BodyOutputType.TrustedHtml,
  };

  private _toastError: Toast = {
    type          : 'error',
    bodyOutputType: BodyOutputType.TrustedHtml,
  };

  constructor(private _ts: ToasterService, private _settings: SettingsService) {
  }

  public message(type: NotificationType, body: string = ''): void {
    if (this._settings.status.openedFromChrome) {
      let nativeEvent = new CustomEvent(`chrome.to.extension.fnd.${type}`, {
        detail: {
          body: body
        }
      });
      document.dispatchEvent(nativeEvent);
    } else {
      switch (type) {
        case NotificationType.FUND_SUCCESS:
          this.success('Transaction \'fund request\' sent.', body);
          break;
        case NotificationType.FUND_FAILED:
          this.error('Transaction \'fund request\' failed.', body);
          break;
      }
    }
  }

  public success(title: string = '', body: string = ''): void {
    this._toastSuccess.title = title;
    this._toastSuccess.body = body;
    this._ts.pop(this._toastSuccess);
  }

  public error(title: string = '', body: string = ''): void {
    this._toastError.title = title;
    this._toastError.body = body;
    this._ts.pop(this._toastError);
  }
}
