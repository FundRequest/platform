import {NgModule} from '@angular/core';
import {SettingsComponent} from './settings/settings.component';
import {RouterModule, Routes} from '@angular/router';
import {SharedModule} from '../../shared/shared.module';
import {ComponentsModule} from '../../components/components.module';

const routes: Routes = [
  {path: '', component: SettingsComponent}
];

@NgModule({
  imports: [
    SharedModule,
    ComponentsModule,
    RouterModule.forChild(routes)
  ],
  declarations: [SettingsComponent],
  exports: [
    RouterModule
  ]
})
export class SettingsModule {
}
