import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";

import {Store} from "@ngrx/store";
import {IState} from "../../redux/store";
import {Observable} from "rxjs/Observable";
import {ClearUser, ReplaceUser} from "../../redux/user.reducer";

import {createUser, IUserRecord} from "../../redux/user.models";
import {ContractsService} from "../contracts/contracts.service";
import {AuthService} from "../../core/auth/auth.service";

@Injectable()
export class UserService {
  private user: IUserRecord = null;

  constructor(private store: Store<IState>,
              private http: HttpClient,
              private authService: AuthService,
              private contractService: ContractsService) {
  }

  public login(jwtToken): void {
    this.authService.login(jwtToken);
    this.initUser();
  }

  public logout(): void {
    this.store.dispatch(new ClearUser());
    this.authService.logout();
  }

  private initUser(): void {
    this.user = createUser();
    if (this.authService.isAuthenticated()) {
      this.http.get(`/api/private/user/info`)
        .take(1).subscribe((user: IUserRecord) => {
        this.user = user;
        this.store.dispatch(new ReplaceUser(this.user));

        this.contractService.getUserBalance().then(balance => {
          console.log('getuserbalance', balance);
          let userObject = JSON.parse(JSON.stringify(this.user));
          userObject.balance = balance;

          this.contractService.getUserAllowance().then(allowance => {
            console.log('getUserAllowance', allowance);
            userObject.allowance = allowance;
            let user: IUserRecord = createUser(userObject);
            this.store.dispatch(new ReplaceUser(user));
          });
        });
      }, error => this.handleError(error));
    }
  }

  public getCurrentUser(): Observable<IUserRecord> {
    if (this.user == null) {
      this.initUser();
    }

    return this.store.select(state => state.user);
  }

  public setAllowance(value: number) {
    this.contractService.setUserAllowance(value).then(allowance => {
      let user: IUserRecord = createUser(JSON.parse(JSON.stringify(this.user)));
      user.asMutable();
      user.allowance = allowance;
      user.asImmutable();
      this.store.dispatch(new ReplaceUser(user));
    });
    this.contractService.setUserAllowance(value);
  }

  private handleError(error: any): void {
    console.error('An error occurred', error);
  }
}


