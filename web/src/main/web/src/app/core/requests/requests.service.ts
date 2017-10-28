import {Injectable} from "@angular/core";
import {RequestsStats} from "./RequestsStats";
import {Request} from "./Request";
import {HttpClient} from "@angular/common/http";

import 'rxjs/add/operator/toPromise';

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

  update(request: Request): Promise<Request> {
    return this.http.post(`/api/private/requests/${request.id}`, request)
      .toPromise()
      .then(response => response as Request)
      .catch(this.handleError);
  }

  getAll(): Promise<Request[]> {
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
