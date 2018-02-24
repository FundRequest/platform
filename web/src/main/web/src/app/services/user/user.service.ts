import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

import {Store} from '@ngrx/store';
import {IState} from '../../redux/store';
import {Observable} from 'rxjs/Observable';
import {ClearUser, ReplaceUser} from '../../redux/user.reducer';

import {createUser, IUserRecord} from '../../redux/user.models';
import {AuthService} from '../../core/auth/auth.service';
import {ApiUrls} from '../../api/api.urls';

@Injectable()
export class UserService {

  private user: IUserRecord = null;

  constructor(private _store: Store<IState>,
    private _http: HttpClient,
    private _as: AuthService) {
  }

  public login(returnUri?: string) {
    this._as.login(returnUri);
  }

  public logout(): void {
    this._store.dispatch(new ClearUser());
    this._as.logout();
  }

  private async initUser(): Promise<void> {
    this.user = createUser();
    if (this._as.isAuthenticated()) {
      let newUser: IUserRecord = await this._http.get(ApiUrls.userInfo).toPromise() as IUserRecord;
      this.user = createUser(newUser);
      this._store.dispatch(new ReplaceUser(this.user));
    }
  }

  public get currentUser$(): Observable<IUserRecord> {
    if (this.user == null) {
      this.initUser();
    }

    return this._store.select(state => state.user);
  }

  private handleError(error: any): void {
    console.error('An error occurred', error);
  }

  public userIsLoggedIn(): boolean {
    return this.user != null && !!this.user.userId;
  }
}


