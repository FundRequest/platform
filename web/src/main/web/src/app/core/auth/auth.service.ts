import { Injectable } from '@angular/core';
import { KeycloakService } from '../../services/keycloak/keycloak.service';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/observable/fromPromise';

@Injectable()
export class AuthService {

  constructor(private _ks: KeycloakService) {
  }

  public get token$(): Observable<string> {
    return Observable.fromPromise(this._ks.getToken());
  }

  public login(returnUri?): void {
    KeycloakService.login(returnUri);
  }

  public logout(): void {
    KeycloakService.logout();
  }

  public isAuthenticated(): boolean {
    return KeycloakService.auth.loggedIn;
  }
}
