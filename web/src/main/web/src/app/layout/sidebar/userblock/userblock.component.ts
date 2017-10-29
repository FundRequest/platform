import {Component, OnInit} from "@angular/core";

import {UserblockService} from "./userblock.service";
import {ContractsService} from "../../../core/contracts/contracts.service";
import {UserService} from "../../../core/user/user.service";

@Component({
    selector: 'app-userblock',
    templateUrl: './userblock.component.html',
    styleUrls: ['./userblock.component.scss']
})
export class UserblockComponent implements OnInit {
    user: any;
    balance: string;

    constructor(public userService: UserService,
                public userblockService: UserblockService) {
        this.user = {
            picture: 'assets/img/user/01.jpg'
        };
    }

    async ngOnInit() {
      this.balance = await this.userService.getBalance();
    }

    userBlockIsVisible(): boolean {
        return this.userblockService.getVisibility();
    }

}
