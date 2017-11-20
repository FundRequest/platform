import { TestBed, inject } from '@angular/core/testing';

import { KeycloakService } from './keycloak.service';

describe('KeycloakService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [KeycloakService]
    });
  });

  it('should be created', inject([KeycloakService], (service: KeycloakService) => {
    expect(service).toBeTruthy();
  }));
});
