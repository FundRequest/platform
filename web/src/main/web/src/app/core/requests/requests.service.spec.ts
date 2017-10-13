/* tslint:disable:no-unused-variable */

import {inject, TestBed} from "@angular/core/testing";
import {RequestsService} from "./requests.service";
import {HttpModule, Response, ResponseOptions, XHRBackend} from "@angular/http";
import {MockBackend} from "@angular/http/testing";
import {Request} from "app/core/requests/Request";

describe('Service: Requests', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpModule],
      providers: [
        {provide: XHRBackend, useClass: MockBackend},
        RequestsService
      ]

    });
  });

  it('Should return requests when creating service', inject([RequestsService, XHRBackend], (service: RequestsService, mockBackend) => {
    let request = new Request();
    request.id = 1;
    request.loggedInUserIsWatcher = true;
    request.technologies = ["typescript"];
    const mockResponse = [request];

    mockBackend.connections.subscribe((connection) => {
      connection.mockRespond(new Response(new ResponseOptions({
        body: JSON.stringify(mockResponse)
      })));
    });

    service.requests.subscribe((r) => {
      expect(r.length).toBe(1);
      expect(r[0]).toEqual(request);
    });

  }));
});
