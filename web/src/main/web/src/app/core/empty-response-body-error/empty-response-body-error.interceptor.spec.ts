import { TestBed, inject } from '@angular/core/testing';

import { EmptyResponseBodyErrorInterceptor } from './empty-response-body-error.interceptor';

describe('EmptyResponseBodyErrorInterceptor', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EmptyResponseBodyErrorInterceptor]
    });
  });

  it('should be created', inject([EmptyResponseBodyErrorInterceptor], (service: EmptyResponseBodyErrorInterceptor) => {
    expect(service).toBeTruthy();
  }));
});
