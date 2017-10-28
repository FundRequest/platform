import {Component, OnInit} from "@angular/core";

import {UserblockService} from "./userblock.service";
import {ContractService} from "../../../core/contracts/contracts.service";

@Component({
    selector: 'app-userblock',
    templateUrl: './userblock.component.html',
    styleUrls: ['./userblock.component.scss']
})
export class UserblockComponent implements OnInit {
    user: any;
    balance: string;

    constructor(public userblockService: UserblockService,
                public contractService: ContractService) {
        this.user = {
            picture: 'assets/img/user/01.jpg'
        };
    }

    async ngOnInit() {
      this.balance = await this.contractService.getUserBalance();
    }

    userBlockIsVisible() {
        return this.userblockService.getVisibility();
    }

}
