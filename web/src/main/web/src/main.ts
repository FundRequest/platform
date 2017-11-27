import './vendor.ts';
import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';
import { KeycloakService } from './app/services/keycloak/keycloak.service';

if (environment.production) {
  enableProdMode();
}

KeycloakService.init()
  .then(() => {
    const p = platformBrowserDynamic().bootstrapModule(AppModule);
    p.then(() => { (<any>window).appBootstrap && (<any>window).appBootstrap(); });
  })
  .catch((error) => console.log(error)); //window.location.reload());
