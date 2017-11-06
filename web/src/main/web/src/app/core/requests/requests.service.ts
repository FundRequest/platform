import {Injectable} from "@angular/core";
import {RequestsStats} from "./RequestsStats";
import {Request} from "./Request";
import {HttpClient, HttpHeaders} from "@angular/common/http";

import 'rxjs/add/operator/toPromise';
import {User} from "../user/User";

@Injectable()
export class RequestsService {

  constructor(private http: HttpClient) {
  }

  getStatistics(): Promise<RequestsStats> {
    return this.http.get('/api/public/requests/statistics')
      .toPromise()
      .then(response => response as RequestsStats)
      .catch(this.handleError);
  }

  get(id: number): Promise<Request> {
    return this.http.get(`/api/private/requests/${id}`)
      .toPromise()
      .then(response => response as Request)
      .catch(this.handleError);
  }

  public getWatchers(requestId: number): Promise<string[]> {
    return this.http.get(`/api/private/requests/${requestId}/watchers`).toPromise();
  }

  public getAll(): Promise<Request[]> {
    return this.http.get(`/api/private/requests`)
      .toPromise()
      .then(response => response as Request[])
      .catch(this.handleError);
  };

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }
}
