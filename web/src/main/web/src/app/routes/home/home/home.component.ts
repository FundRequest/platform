import {Component, OnInit} from "@angular/core";
import {Router, ActivatedRoute} from '@angular/router';

import {ColorsService} from "../../../shared/colors/colors.service";
import {RequestsService} from "../../../core/requests/requests.service";

import {LocalStorageService} from 'angular-2-local-storage';
import {JwtHelper} from "angular2-jwt/angular2-jwt";
import {AuthService} from "../../../core/auth/auth.service";
import {Observable} from "rxjs/Observable";
import {RequestsStat} from "../../../core/requests/RequestsStat";
import {RequestsStats} from "../../../core/requests/RequestsStats";

declare var civic: any;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  options = {
    /* animate: {
     duration: 800,
     enabled: true
     },
     barColor: this.colors.byName('info'),
     trackColor: 'rgba(200,200,200,0.4)',
     scaleColor: false,
     lineWidth: 10,
     lineCap: 'round',
     size: 145*/
  };

  /*
   methods: {
   addFND(amount) {
   return amount + ' FND';
   },
   fromWei(amountInWei) {
   var number = Number(amountInWei) / 1000000000000000000;
   return ((Math.round(number * 100) / 100).toFixed(2)).toLocaleString();
   },
   round(amount, digitsAfterDecimal) {
   if (typeof digitsAfterDecimal === 'undefined') {
   digitsAfterDecimal = 2;
   }
   var number = Number(amount);
   return ((Math.round(number * 100) / 100).toFixed(digitsAfterDecimal)).toLocaleString()
   }
   */
  private statistics: Observable<RequestsStats>;

  private numberOfRequests: RequestsStat;
  private requestsFunded: RequestsStat;
  private numberOfFunders: RequestsStat;
  private totalAmountFunded: RequestsStat;
  private averageFundingPerRequest: RequestsStat;
  private percentageFunded: RequestsStat;

  constructor(private localStorageService: LocalStorageService,
              private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService,
              public colors: ColorsService,
              public requestsService: RequestsService) {
    this.statistics = requestsService.getStatistics();
    this.statistics.subscribe((stats) => (
        this.numberOfRequests = stats.numberOfRequests,
          this.requestsFunded = stats.requestsFunded,
          this.numberOfFunders = stats.numberOfFunders,
          this.totalAmountFunded = stats.totalAmountFunded,
          this.averageFundingPerRequest = stats.averageFundingPerRequest,
          this.percentageFunded = stats.percentageFunded
      )
    );
  }

  ngOnInit() {

    if (!this.authService.isAuthenticated()) {
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
  }
}
