import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {RequestsStats} from "./RequestsStats";
import {Request} from "./Request";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class RequestsService {

  //public requests: Observable<Array<Request>>;
  //public statistics: Observable<RequestsStats>;

  constructor(private http: HttpClient) {
    //this.requests = this.http.get('http://localhost:8080/api/requests').map(data => data.json());
    //this.statistics = this.http.get('http://localhost:8080/api/requests/statistics').map(data => data.json());
  }

  getStatistics(): Observable<RequestsStats> {
      return this.http.get('/api/public/requests/statistics');
  }

  get(id : number): Observable<Request> {
      return this.http.get(`/api/private/requests/${id}`);
  }

}
