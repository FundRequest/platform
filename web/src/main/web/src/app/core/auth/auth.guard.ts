import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { UserService } from '../../services/user/user.service';

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private userService: UserService, private authService: AuthService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (!this.authService.isAuthenticated()) {
      //this.userService.login(state.url);
      return false;
    } else {
      return true;
    }
  }
}


