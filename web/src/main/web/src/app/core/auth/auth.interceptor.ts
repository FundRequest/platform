import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';

import {Observable} from 'rxjs/Observable';
import {AuthService} from './auth.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(private _as: AuthService) {
  }

  private getRequest(request, headers) {
    let restApiLocation: string = document.baseURI.startsWith('http://localhost:4200') ? 'http://localhost:8080' : '';

    return request.clone({
      setHeaders: headers,
      url: /*request.url.startsWith('/api/') ||*/ request.url.startsWith('/') ? restApiLocation + request.url : request.url
    });
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let headers: any = {};
    console.log(request.url, request.url.indexOf('https://github.com/'));
    if (request.url.indexOf('/api/private') > -1) {
      headers['Access-Control-Allow-Origin'] = '*';
      return this._as.token$
        .mergeMap((token: string) => {
          headers.Authorization = `Bearer ${token}`;
          return next.handle(this.getRequest(request, headers));
        })
        .catch((e: any) => Observable.throw(console.log(e)));
    } else if (request.url.indexOf('https://github.com/') == 0) {
      headers['Access-Control-Allow-Origin'] = '*';
    }

    return next.handle(this.getRequest(request, headers));
  }
}
