import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Store} from '@ngrx/store';
import {IState} from '../../redux/store';
import {Observable} from 'rxjs/Observable';
import {ClearUser, ReplaceUser} from '../../redux/user.reducer';

import {createUser, IUserRecord} from '../../redux/user.models';
import {ContractsService} from '../contracts/contracts.service';
import {AuthService} from '../../core/auth/auth.service';

@Injectable()
export class UserService {

  private user: IUserRecord = null;

  constructor(private store: Store<IState>,
              private http: HttpClient,
              private _as: AuthService,
              private _cs: ContractsService) {
  }

  public login(returnUri?: string) {
    this._as.login(returnUri);
  }

  public logout(): void {
    this.store.dispatch(new ClearUser());
    this._as.logout();
  }

  private async initUser(): Promise<void> {
    this.user = createUser();

    if (this._as.isAuthenticated()) {
      let newUser: IUserRecord = await this.http.get(`/api/private/user/info`).toPromise() as IUserRecord;
      this.user = createUser(newUser);
      this.store.dispatch(new ReplaceUser(this.user));

      let balance;
      let allowance;
      await Promise.all([
        this._cs.getUserBalance().then(result => balance = result),
        this._cs.getUserAllowance().then(result => allowance = result)
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
    let allowance = await this._cs.setUserAllowance(value);
    this.user = this.user.set('allowance', allowance);
    this.store.dispatch(new ReplaceUser(this.user));
    this._cs.setUserAllowance(value);
  }

  private handleError(error: any): void {
    console.error('An error occurred', error);
  }

  public userIsLoggedIn(): boolean {
    return this.user != null && !!this.user.userId;
  }
}


