import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

import {Store} from "@ngrx/store";
import {IState} from "../../redux/store";
import {Observable} from "rxjs/Observable";
import {ClearUser, ReplaceUser} from "../../redux/user.reducer";

import {createUser, IUserRecord} from "../../redux/user.models";
import {ContractsService} from "../contracts/contracts.service";
import {AuthService} from "../../core/auth/auth.service";
import {Router} from "@angular/router";

declare let keycloak: any;

@Injectable()
export class UserService {

  private user: IUserRecord = null;

  constructor(private router: Router,
              private store: Store<IState>,
              private http: HttpClient,
              private authService: AuthService,
              private contractService: ContractsService) {
    const self = this;
    if (keycloak.token) {
      self.authService.login(keycloak.token);
      self.initUser();
    }
  }

  public login(returnUrl?: string) {
    const self = this;
    keycloak.login();
  }

  public logout(): void {
    this.store.dispatch(new ClearUser());
    window.location.href = keycloak.authServerUrl + "/realms/fundrequest/protocol/openid-connect/logout?redirect_uri="+window.location.href;
  }

  private async initUser(): Promise<void> {
    this.user = createUser();
    if (this.authService.isAuthenticated()) {
      let newUser: IUserRecord = await this.http.get(`/api/private/user/info`).toPromise() as IUserRecord;
      this.user = createUser(newUser);
      this.store.dispatch(new ReplaceUser(this.user));

      let balance;
      let allowance;
      await Promise.all([
        this.contractService.getUserBalance().then(result => balance = result),
        this.contractService.getUserAllowance().then(result => allowance = result)
      ]).catch(error => this.handleError(error));

      this.user = this.user.set('balance', balance);
      this.user = this.user.set('allowance', allowance);
      this.store.dispatch(new ReplaceUser(this.user));
    }
  }

  public getCurrentUser(): Observable<IUserRecord> {
    if (this.user == null) {
      this.initUser();
    }

    return this.store.select(state => state.user);
  }

  public async setAllowance(value: number) {
    let allowance = await this.contractService.setUserAllowance(value);
    this.user = this.user.set('allowance', allowance);
    this.store.dispatch(new ReplaceUser(this.user));
    this.contractService.setUserAllowance(value);
  }

  private handleError(error: any): void {
    console.error('An error occurred', error);
  }
}


