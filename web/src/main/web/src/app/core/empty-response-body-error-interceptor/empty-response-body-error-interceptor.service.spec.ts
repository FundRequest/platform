import { TestBed, inject } from '@angular/core/testing';

import { EmptyResponseBodyErrorInterceptorService } from './empty-response-body-error-interceptor.service';

describe('EmptyResponseBodyErrorInterceptorService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EmptyResponseBodyErrorInterceptorService]
    });
  });

  it('should be created', inject([EmptyResponseBodyErrorInterceptorService], (service: EmptyResponseBodyErrorInterceptorService) => {
    expect(service).toBeTruthy();
  }));
});
