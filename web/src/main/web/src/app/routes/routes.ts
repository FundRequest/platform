import {LayoutComponent} from "../layout/layout.component";
import {AuthGuard} from "../core/auth/auth.guard";

export const routes = [

    {
        path: '',
        component: LayoutComponent,
        children: [
            { path: '', redirectTo: 'home', pathMatch: 'full' },
            { path: 'home', loadChildren: './home/home.module#HomeModule' },
            { path: 'requests', loadChildren: './requests/requests.module#RequestsModule', canActivate: [AuthGuard] },
        ]
    },

    // Not found
    { path: '**', redirectTo: 'home' }

];
