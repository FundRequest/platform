import {Injectable} from '@angular/core';

@Injectable()
export class AuthService {
  private jwtToken: string;

  public getToken(): any {
    return this.jwtToken;
  }

  public login(jwtToken: string): void {
    this.jwtToken = jwtToken;
  }

  public logout(): void {
    this.jwtToken = null;
  }

  public isAuthenticated(): boolean {
    return this.jwtToken != null;

  }
}
