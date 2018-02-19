import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';

import {Observable} from 'rxjs/Observable';
import {AuthService} from './auth.service';

/**
 * Interceptor that add extra headers and the user's authorization token when calling private locations
 * @implements HttpInterceptor
 */
@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  /**
   * @param {AuthService} _as
   */
  constructor(private _as: AuthService) {
  }

  private static _getRequest(request, headers) {
    let restApiLocation: string = document.baseURI.startsWith('http://localhost:4200') ? 'http://localhost:8080' : '';

    return request.clone({
      setHeaders: headers,
      url: /*request.url.startsWith('/api/') ||*/ request.url.startsWith('/') ? restApiLocation + request.url : request.url
    });
  }

  /**
   * @param {HttpRequest<any>} request
   * @param {HttpHandler} next
   * @returns {Observable<HttpEvent<any>>}
   */
  public intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let headers: any = {};
    if (request.url.indexOf('/api/private') > -1) {
      headers['Access-Control-Allow-Origin'] = '*';
      return this._as.token$
        .mergeMap((token: string) => {
          headers.Authorization = `Bearer ${token}`;
          return next.handle(AuthInterceptor._getRequest(request, headers));
        })
        .catch((e: any) => Observable.throw(console.log(e)));
    } else if (request.url.indexOf('https://github.com/') == 0) {
      headers['Access-Control-Allow-Origin'] = '*';
    }

    return next.handle(AuthInterceptor._getRequest(request, headers));
  }
}
