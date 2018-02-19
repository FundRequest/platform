import { Injectable } from '@angular/core';
import {
  HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest,
  HttpResponse
} from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

/**
 * Intercepter to catch dummy errors. In some calls there is an error response between 200 and 300 that are not really errors.
 * @implements HttpInterceptor
 */
@Injectable()
export class EmptyResponseBodyErrorInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req)
      .catch((err: HttpErrorResponse) => {
        if (err.status >= 200 && err.status < 300) {
          const res = new HttpResponse({
            body      : null,
            headers   : err.headers,
            status    : err.status,
            statusText: err.statusText,
            url       : err.url
          });

          return Observable.of(res);
        } else {
          return Observable.throw(err);
        }
      });
  }
}
