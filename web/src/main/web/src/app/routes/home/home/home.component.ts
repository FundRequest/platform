import {Component} from "@angular/core";
import {ActivatedRoute, Router} from '@angular/router';

import {AuthService} from "../../../core/auth/auth.service";
import {RequestsStats} from "../../../core/requests/RequestsStats";
import {RequestService} from "../../../services/request/request.service";
import {UserService} from "../../../services/user/user.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  private _statistics: Promise<RequestsStats>;

  constructor(private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService,
              private requestService: RequestService,
              private userService: UserService) {
    /*route.params.subscribe(val => {
      if (!this.authService.isAuthenticated()) {
        this.login();
      }
    });*/
  }

  get statistics(): Promise<RequestsStats> {
    if (!this._statistics) {
      this._statistics = (async () => await this.requestService.getStatistics() as RequestsStats)();
    }
    return this._statistics;
  }
}
