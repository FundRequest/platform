import { NgModule, Optional, SkipSelf } from '@angular/core';

import { SettingsService } from './settings/settings.service';
import { ThemesService } from './themes/themes.service';
import { TranslatorService } from './translator/translator.service';
import { MenuService } from './menu/menu.service';

import { throwIfAlreadyLoaded } from './module-import-guard';
import { AuthService } from './auth/auth.service';
import { AuthGuard } from './auth/auth.guard';
import { HttpClientModule } from '@angular/common/http';
import { ComponentsModule } from '../components/components.module';

@NgModule({
  imports     : [
    HttpClientModule,
    ComponentsModule
  ],
  providers   : [
    SettingsService,
    ThemesService,
    TranslatorService,
    MenuService,
    AuthService,
    AuthGuard
  ],
  declarations: [],
  exports     : []
})

export class CoreModule {
  constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    throwIfAlreadyLoaded(parentModule, 'CoreModule');
  }
}
