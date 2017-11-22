import { TestBed, inject } from '@angular/core/testing';

import { NotificationStreamService } from './notification-stream.service';

describe('NotificationStreamService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NotificationStreamService]
    });
  });

  it('should be created', inject([NotificationStreamService], (service: NotificationStreamService) => {
    expect(service).toBeTruthy();
  }));
});
