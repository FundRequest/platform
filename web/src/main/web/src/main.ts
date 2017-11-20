import "./vendor.ts";
import {enableProdMode} from "@angular/core";
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic";

import {AppModule} from "./app/app.module";
import {environment} from "./environments/environment";
import {KeycloakService} from "./app/services/keycloak/keycloak.service";

if (environment.production) {
    enableProdMode();
}

// .catch(err => console.error(err));
KeycloakService.init()
  .then(() => {
    const p = platformBrowserDynamic().bootstrapModule(AppModule);
    p.then(() => { (<any>window).appBootstrap && (<any>window).appBootstrap(); })
  })
  .catch(() => window.location.reload());
