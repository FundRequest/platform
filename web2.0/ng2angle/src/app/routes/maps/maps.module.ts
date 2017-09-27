import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AgmCoreModule } from '@agm/core';

import { SharedModule } from '../../shared/shared.module';
import { GoogleComponent } from './google/google.component';
import { VectorComponent } from './vector/vector.component';

const routes: Routes = [
    { path: 'google', component: GoogleComponent },
    { path: 'vector', component: VectorComponent }
];

@NgModule({
    imports: [
        SharedModule,
        RouterModule.forChild(routes),
        AgmCoreModule.forRoot({
            apiKey: 'AIzaSyBNs42Rt_CyxAqdbIBK0a5Ut83QiauESPA'
        })
    ],
    declarations: [
        GoogleComponent,
        VectorComponent
    ],
    exports: [
        RouterModule
    ]
})
export class MapsModule { }