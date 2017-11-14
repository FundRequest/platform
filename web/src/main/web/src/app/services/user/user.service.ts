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

declare var civic: any;

@Injectable()
export class UserService {
  private user: IUserRecord = null;

  constructor(private router: Router,
              private store: Store<IState>,
              private http: HttpClient,
              private authService: AuthService,
              private contractService: ContractsService) {
  }

  public login(returnUrl?: string) {
    const self = this;

    const civicSip = new civic.sip({appId: 'S1wUxaf2b'});
    civicSip.signup({style: 'popup', scopeRequest: civicSip.ScopeRequests.BASIC_SIGNUP});
    // Listen for data
    civicSip.on('auth-code-received', function (event) {
      self.authService.login(event.response);
      self.initUser();

      if (returnUrl) {
        self.router.navigate([returnUrl]);
      }
    });

    civicSip.on('user-cancelled', function (event) {
    });

    // Error events.
    civicSip.on('civic-sip-error', function (error) {
      console.log('   Error type = ' + error.type);
      console.log('   Error message = ' + error.message);
    });
  }

  public logout(): void {
    this.store.dispatch(new ClearUser());
    this.authService.logout();
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
    this.user =  this.user.set('allowance', allowance);
    this.store.dispatch(new ReplaceUser(this.user));
    this.contractService.setUserAllowance(value);
  }

  private handleError(error: any): void {
    console.error('An error occurred', error);
  }
}


