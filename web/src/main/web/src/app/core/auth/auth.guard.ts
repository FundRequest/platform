import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { UserService } from '../../services/user/user.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private _us: UserService, private _as: AuthService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (!this._as.isAuthenticated()) {
      //this._us.login(state.url);
      return false;
    } else {
      return true;
    }
  }
}


