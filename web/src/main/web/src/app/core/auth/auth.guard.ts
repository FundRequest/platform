import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from '@angular/router';
import {AuthService} from './auth.service';
import {UserService} from '../../services/user/user.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private _us: UserService, private _as: AuthService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this._as.isAuthenticated();
  }
}


