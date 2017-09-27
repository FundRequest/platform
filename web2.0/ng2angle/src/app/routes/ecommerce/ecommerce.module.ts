import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';
import { OrdersComponent } from './orders/orders.component';
import { OrderviewComponent } from './orderview/orderview.component';
import { ProductsComponent } from './products/products.component';
import { ProductviewComponent } from './productview/productview.component';
import { CheckoutComponent } from './checkout/checkout.component';

const routes: Routes = [
    { path: 'orders', component: OrdersComponent },
    { path: 'orderview', component: OrderviewComponent },
    { path: 'products', component: ProductsComponent },
    { path: 'productview', component: ProductviewComponent },
    { path: 'checkout', component: CheckoutComponent }
];

@NgModule({
    imports: [
        SharedModule,
        RouterModule.forChild(routes)
    ],
    declarations: [
        OrdersComponent,
        OrderviewComponent,
        ProductsComponent,
        ProductviewComponent,
        CheckoutComponent
    ],
    exports: [
        RouterModule
    ]
})
export class EcommerceModule { }
