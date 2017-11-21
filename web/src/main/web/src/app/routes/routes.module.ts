import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PagesModule } from './pages/pages.module';
import { MenuService } from '../core/menu/menu.service';
import { SharedModule } from '../shared/shared.module';
import { TranslatorService } from '../core/translator/translator.service';

import { menu } from './menu';
import { routes } from './routes';

@NgModule({
  imports     : [
    SharedModule,
    RouterModule.forRoot(routes, {useHash: true}),
    PagesModule
  ],
  declarations: [],
  exports     : [
    RouterModule
  ]
})

export class RoutesModule {
  constructor(public menuService: MenuService, public tr: TranslatorService) {
    menuService.addMenu(menu);
  }
}
