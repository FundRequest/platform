/* tslint:disable:no-unused-variable */

import {async, inject, TestBed} from '@angular/core/testing';

import {UserblockComponent} from './userblock.component';
import {UserblockService} from './userblock.service';
import {AccountWeb3Service} from '../../services/accountWeb3/account-web3.service';

describe('Component: Userblock', () => {

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserblockService, AccountWeb3Service]
    }).compileComponents();
  });

  it('should create an instance', async(inject([UserblockService, AccountWeb3Service], (userBlockService, accountWeb3Service) => {
    let component = new UserblockComponent(userBlockService, accountWeb3Service);
    expect(component).toBeTruthy();
  })));
});
