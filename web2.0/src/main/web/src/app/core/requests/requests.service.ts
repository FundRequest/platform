import {Injectable} from "@angular/core";
import {Http} from "@angular/http";
import {Request} from "app/core/requests/Request";
import {Observable} from "rxjs/Observable";

@Injectable()
export class RequestsService {

  public requests: Observable<Array<Request>>;
  private _http: Http;

  constructor(http: Http) {
    this._http = http;
    this.requests = this._http.get('http://localhost:8080/requests').map(data => data.json());
  }

}
