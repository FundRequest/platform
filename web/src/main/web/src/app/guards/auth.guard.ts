import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import {LocalStorageService} from "angular-2-local-storage";

@Injectable()
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private localStorageService: LocalStorageService,) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this.localStorageService.get('jwt')) {
      // logged in so return true
      return true;
    }

    // not logged in so redirect to home page with the return url
    this.router.navigate(['/home'], { queryParams: { returnUrl: state.url }});
    return false;
  }
}
