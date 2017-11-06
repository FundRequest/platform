import {Injectable} from '@angular/core';
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';

import {Observable} from 'rxjs/Observable';
import {AuthService} from "./auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(public auth: AuthService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let headers: any = {};
    headers['Access-Control-Allow-Origin'] = "*";

    if (request.url.indexOf('/api/private') > -1) {
      headers.Authorization = `Bearer ${this.auth.getToken()}`;
    }

    request = request.clone({
      setHeaders: headers
    });

    return next.handle(request);
  }
}
