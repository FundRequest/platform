import { Injectable } from '@angular/core';
import {ContractsService} from "../contracts/contracts.service";
import {User} from "./User";
import {HttpClient} from "@angular/common/http";

@Injectable()
export class UserService {

  public user: Promise<User>;

  constructor(private contractsService: ContractsService, private http: HttpClient) {
  }

  public getBalance(): Promise<string> {
    return this.contractsService.getUserBalance();
  }

  public getAllowance(): Promise<string> {
    return this.contractsService.getUserAllowance();
  }


  public setAllowance(value: number): Promise<string> {
    return this.contractsService.setUserAllowance(value);
  }

  public async getUserInfo(): Promise<User> {
    if(this.user == null) {
      this.user = this.http.get(`/api/private/user/info`).toPromise() as Promise<User>;
    }

    return this.user;
  }
}


