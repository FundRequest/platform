import {Component, OnInit} from "@angular/core";

import {UserblockService} from "./userblock.service";
import {ContractsService} from "../../../core/contracts/contracts.service";
import {UserService} from "../../../core/user/user.service";
import {User} from "../../../core/user/User";

@Component({
    selector: 'app-userblock',
    templateUrl: './userblock.component.html',
    styleUrls: ['./userblock.component.scss']
})
export class UserblockComponent implements OnInit {
    user: Promise<User>;
    balance: string;

    constructor(public userService: UserService,
                public userblockService: UserblockService) {
    }

    async ngOnInit() {
      this.user = this.userService.getUserInfo();
      this.balance = await this.userService.getBalance();
    }

    userBlockIsVisible(): boolean {
        return this.userblockService.getVisibility();
    }

}
