/* tslint:disable:no-unused-variable */

import {inject, TestBed} from "@angular/core/testing";
import {UserblockService} from "./userblock.service";

describe('Service: Userblock', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserblockService]
    });
  });

  it('should ...', inject([UserblockService], (service: UserblockService) => {
    expect(service).toBeTruthy();
  }));
});
