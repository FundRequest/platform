import {Component, OnInit} from "@angular/core";
import {ActivatedRoute, Router} from '@angular/router';

import {RequestsService} from "../../../core/requests/requests.service";

import {LocalStorageService} from 'angular-2-local-storage';
import {AuthService} from "../../../core/auth/auth.service";
import {RequestsStats} from "../../../core/requests/RequestsStats";
import {ContractsService} from "../../../core/contracts/contracts.service";

declare var civic: any;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  private _statistics: Promise<RequestsStats>;

  constructor(private localStorageService: LocalStorageService,
              private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService,
              public requestsService: RequestsService) {
  }

  ngOnInit() {
    if (!this.authService.isAuthenticated()) {
      this.login();
    }
  }

  login(): void {
    const localStorageService: LocalStorageService = this.localStorageService;
    const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    const civicSip = new civic.sip({appId: 'S1wUxaf2b'});
    const router = this.router;
    civicSip.signup({style: 'popup', scopeRequest: civicSip.ScopeRequests.BASIC_SIGNUP});
    // Listen for data
    civicSip.on('auth-code-received', function (event) {
      const jwtToken = event.response;
      localStorageService.set('id_token', jwtToken);
      router.navigate([returnUrl]);
    });

    civicSip.on('user-cancelled', function (event) {
    });

    // Error events.
    civicSip.on('civic-sip-error', function (error) {
      console.log('   Error type = ' + error.type);
      console.log('   Error message = ' + error.message);
    });
  }

  get statistics(): Promise<RequestsStats> {
    if (!this._statistics) {
      this._statistics = (async () => await this.requestsService.getStatistics() as RequestsStats)();
    }
    return this._statistics;
  }
}
