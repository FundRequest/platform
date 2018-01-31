import { TestBed, inject } from '@angular/core/testing';

import { AccountWeb3Service } from './account-web3.service';

describe('AccountWeb3Service', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AccountWeb3Service]
    });
  });

  it('should be created', inject([AccountWeb3Service], (service: AccountWeb3Service) => {
    expect(service).toBeTruthy();
  }));
});
