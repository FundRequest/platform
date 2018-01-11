import {Injectable} from '@angular/core';
import {environment} from '../../../environments/environment';
import * as Keycloak from 'keycloak-js';

@Injectable()
export class KeycloakService {
  static auth: any = {};

  constructor() {
  }

  public static init(): Promise<any> {
    const keycloakAuth: any = Keycloak(environment.keycloak);

    KeycloakService.auth.loggedIn = false;

    return new Promise((resolve, reject) => {
      keycloakAuth.init({checkLoginIframe: false, flow: 'implicit'})
        .success((authenticated) => {
          KeycloakService.auth.authz = keycloakAuth;
          if (keycloakAuth.token) {
            KeycloakService.auth.loggedIn = true;
          }
          KeycloakService.auth.logoutUrl = keycloakAuth.authServerUrl
            + `/realms/${environment.keycloak.realm}/protocol/openid-connect/logout?redirect_uri=${document.baseURI}`;
          resolve();
        })
        .error(() => {
          reject();
        });
    });
  }

  public static logout() {
    KeycloakService.auth.loggedIn = false;
    KeycloakService.auth.authz = null;

    window.location.href = KeycloakService.auth.logoutUrl;
  }

  public static login(returnUri?): void {
    if (KeycloakService.auth.authz == null) {
      this.init();
    } else {
      if (returnUri != null) {
        KeycloakService.auth.authz.login({returnUri: returnUri});
      } else {
        KeycloakService.auth.authz.login();
      }
    }
  }

  public getToken(): Promise<string> {
    return new Promise((resolve, reject) => {
      if (KeycloakService.auth.authz && KeycloakService.auth.authz.token) {
        resolve(<string>KeycloakService.auth.authz.token);
        KeycloakService.auth.authz
          .updateToken(5)
          .success(() => {
            resolve(<string>KeycloakService.auth.authz.token);
          })
          .error(() => {
            reject('Failed to refresh token');
          });
      } else {
        reject('Not logged in');
      }
    });
  }

  public static getUsername(): string {
    return KeycloakService.auth.authz.tokenParsed.preferred_username;
  }

  public static getFullName(): string {
    return KeycloakService.auth.authz.tokenParsed.name;
  }

}
