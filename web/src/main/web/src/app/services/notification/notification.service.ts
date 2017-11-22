import { Injectable } from '@angular/core';
import { BodyOutputType, Toast, ToasterConfig, ToasterService } from 'angular2-toaster';

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

  constructor(private _ts: ToasterService) {
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
