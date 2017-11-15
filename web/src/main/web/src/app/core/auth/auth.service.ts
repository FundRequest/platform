import {Injectable} from '@angular/core';
import {tokenNotExpired} from "angular2-jwt";
import {LocalStorageService} from "angular-2-local-storage";
import {Router} from "@angular/router";

@Injectable()
export class AuthService {
  constructor(private router: Router,
              private localStorageService: LocalStorageService) {
  }

  public getToken(): any {
    return this.localStorageService.get('id_token');
  }

  public login(jwtToken: string): void {
    this.localStorageService.set('id_token', jwtToken);
  }

  public logout(): void {
    this.localStorageService.remove('id_token');
    this.router.navigate(['/home']);
  }

  public isAuthenticated(): boolean {
    // get the token
    const token = this.getToken();
    // return a boolean reflecting
    // whether or not the token is expired
    try {
      return tokenNotExpired(null, token);
    } catch (e) {
      return false;
    }

  }
}
