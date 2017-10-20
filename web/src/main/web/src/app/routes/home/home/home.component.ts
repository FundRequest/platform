import {Component, OnInit} from "@angular/core";
import {Router, ActivatedRoute} from '@angular/router';

import {ColorsService} from "../../../shared/colors/colors.service";
import {RequestsService} from "../../../core/requests/requests.service";

import {LocalStorageService} from 'angular-2-local-storage';
import {AuthService} from "../../../core/auth/auth.service";
import {RequestsStats} from "../../../core/requests/RequestsStats";

declare var civic: any;

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  pieOptions = {
    animate: {
      duration: 800,
      enabled: true
    },
    barColor: '#fff',
    trackColor: 'rgba(93,156,236,0.4)',
    scaleColor: false,
    lineWidth: 5,
    lineCap: 'round',
    size: 55
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
  public statistics: RequestsStats = new RequestsStats();
  public percentageFunded: number;

  constructor(private localStorageService: LocalStorageService,
              private route: ActivatedRoute,
              private router: Router,
              private authService: AuthService,
              public colors: ColorsService,
              public requestsService: RequestsService) {
    requestsService.getStatistics().subscribe((stats) => {
        this.statistics = stats;
        this.percentageFunded = stats.percentageFunded
      }
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
