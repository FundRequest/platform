import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';

import {Observable} from 'rxjs/Observable';
import {AuthService} from "./auth.service";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  constructor(public auth: AuthService) {
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    let headers: any = {};
    headers['Access-Control-Allow-Origin'] = "*";

    if (request.url.startsWith('/api/private')) {
      headers.Authorization = `Bearer ${this.auth.getToken()}`;
    }

    request = request.clone({
      setHeaders: headers,
      url: request.url.startsWith('/api/') ? 'http://localhost:8080' + request.url : request.url
    });

    return next.handle(request);
  }
}
