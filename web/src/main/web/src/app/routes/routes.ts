import {LayoutComponent} from '../layout/layout.component';
import {Error404Component} from './pages/error404/error404.component';
import {Error500Component} from './pages/error500/error500.component';
import {AuthGuard} from '../core/auth/auth.guard';

export const routes = [

  {
    path     : '',
    component: LayoutComponent,
    children : [
      {path: '', redirectTo: 'home', pathMatch: 'full'},
      {path: 'home', loadChildren: './home/home.module#HomeModule'},
      {path: 'settings', loadChildren: './settings/settings.module#SettingsModule', canActivate: [AuthGuard]},
      {path: 'requests', loadChildren: './requests/requests.module#RequestsModule'},
    ]
  },

  // Not lazy-loaded routes
  {path: '404', component: Error404Component},
  {path: '500', component: Error500Component},

  // Not found
  {path: '**', redirectTo: 'home'}

];
