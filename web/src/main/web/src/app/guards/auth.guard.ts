import {Injectable} from '@angular/core';
import {Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot} from '@angular/router';
import {LocalStorageService} from "angular-2-local-storage";
import {JwtHelper} from "angular2-jwt";

@Injectable()
export class AuthGuard implements CanActivate {
  jwtHelper: JwtHelper;

  constructor(private router: Router, private localStorageService: LocalStorageService) {
    this.jwtHelper = new JwtHelper();
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    const token: any = this.localStorageService.get('id_token');
    let isValidToken: boolean = false;

    try {
      isValidToken = !this.jwtHelper.isTokenExpired(token);
    } catch (e) {
      console.log(e);
    }

    if (isValidToken) {
      return true;
    }

    // not logged in so redirect to home page with the return url
    this.router.navigate(['/home'], { queryParams: { returnUrl: state.url }});
    return false;
  }
}
