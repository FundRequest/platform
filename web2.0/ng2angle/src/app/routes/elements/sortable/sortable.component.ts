import { Component, OnInit } from '@angular/core';
import { Product } from './sortable.product';

@Component({
    selector: 'app-sortable',
    templateUrl: './sortable.component.html',
    styleUrls: ['./sortable.component.scss']
})
export class SortableComponent implements OnInit {

    availableProducts: Array<Product> = [];
    shoppingBasket: Array<Product> = [];

    listOne: Array<string> = ['Coffee', 'Orange Juice', 'Red Wine', 'Unhealty drink!', 'Water'];

    listBoxers: Array<string> = ['Sugar Ray Robinson', 'Muhammad Ali', 'George Foreman', 'Joe Frazier', 'Jake LaMotta', 'Joe Louis', 'Jack Dempsey', 'Rocky Marciano', 'Mike Tyson', 'Oscar De La Hoya'];
    listTeamOne: Array<string> = [];
    listTeamTwo: Array<string> = [];

    constructor() {
        this.initProducts();
    }

    initProducts() {
        this.availableProducts.push(new Product('Blue Shoes', 3, 35));
        this.availableProducts.push(new Product('Good Jacket', 1, 90));
        this.availableProducts.push(new Product('Red Shirt', 5, 12));
        this.availableProducts.push(new Product('Blue Jeans', 4, 60));
    }

    orderedProduct($event) {
        let orderedProduct: Product = $event.dragData;
        orderedProduct.quantity--;
    }

    addToBasket($event) {
        let newProduct: Product = $event.dragData;
        for (let indx in this.shoppingBasket) {
            let product: Product = this.shoppingBasket[indx];
            if (product.name === newProduct.name) {
                product.quantity++;
                return;
            }
        }
        console.log('adding ' + newProduct);
        this.shoppingBasket.push(new Product(newProduct.name, 1, newProduct.cost));
    }

    totalCost(): number {
        let cost = 0;
        for (let indx in this.shoppingBasket) {
            let product: Product = this.shoppingBasket[indx];
            cost += (product.cost * product.quantity);
        }
        return cost;
    }

    resetBasket() {
        this.availableProducts = [];
        this.shoppingBasket = [];
        this.initProducts();
    }

    ngOnInit() {
    }

}
