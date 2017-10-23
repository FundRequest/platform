import {ApplicationRef, Injectable} from "@angular/core";
import {RequestsStats} from "./RequestsStats";
import {Request} from "./Request";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class RequestsService {

  constructor(private http: HttpClient, private ref: ApplicationRef) {
  }

  getStatistics(): RequestsStats {
    let statistics: RequestsStats = new RequestsStats();
    this.http.get('/api/public/requests/statistics').subscribe((stats) => {
      statistics.fillFromJSON(stats);
    });

    return statistics;
  }

  get(id: number): Request {
    let request = new Request();
    this.http.get(`/api/private/requests/${id}`).subscribe((response) => {
      request.fillFromJSON(response);
    });

    return request;
  }

  async getAll(): Promise<Array<Request>> {
    let requests = [];
    await this.http.get(`/api/private/requests`).subscribe((response: Array<Request>) => {
      response.forEach(function (requestJson) {
        let request: Request = new Request();
        request.fillFromJSON(requestJson);
        requests.push(request);
      });
    });
    return requests;
  };
}
