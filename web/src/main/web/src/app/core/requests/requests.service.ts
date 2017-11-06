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

  async add(issueLink: string, technologies: string[]): Promise<Request> {
    let myNewRequest: Request;
    let location: string;

    await this.http.post(`/api/private/requests/`, {
      issueLink: issueLink,
      technologies: technologies
    }, {observe: 'response'})
      .toPromise()
      .then((response) => {
        location = response.headers.get('location');
      }).catch(this.handleError);

    if (location && location.length > 0) {
      await this.http.get(location)
        .toPromise()
        .then((request: Request) => {
          console.log(request);
          myNewRequest = request;
        })
        .catch(this.handleError);
    }

    return myNewRequest;
  }

  public getWatchers(requestId: number): Promise<string[]> {
    return this.http.get(`/api/private/requests/${requestId}/watchers`).toPromise();
  }

  public setUserAsWatcher(request: Request, user: User): Promise<string[]> {
    return this.http.put(`/api/private/requests/${request.id}/watchers`, {
      responseType: 'text',
      requestId: request.id
    }).toPromise()
      .then(response => {
        request.watchers.push(user.email);
        return request.watchers as string[];
      })
      .catch(this.handleError);
  }

  public async unSetUserAsWatcher(request: Request, user: User): Promise<string[]> {
    return this.http.delete(`/api/private/requests/${request.id}/watchers`, {
      responseType: 'text'
    }).toPromise()
      .then(response => (request.watchers = request.watchers.filter(watcher => watcher != user.email) as string[]))
      .catch(this.handleError);
  }

  public update(request: Request): Promise<Request> {
    return this.http.put(`/api/private/requests/${request.id}`, request)
      .toPromise()
      .then(response => response as Request)
      .catch(this.handleError);
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
