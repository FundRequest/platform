import {Component, OnInit} from "@angular/core";
import {Router, ActivatedRoute} from '@angular/router';

import {ColorsService} from "../../../shared/colors/colors.service";
import {RequestsService} from "../../../core/requests/requests.service";

import {LocalStorageService} from 'angular-2-local-storage';
import {JwtHelper} from "angular2-jwt/angular2-jwt";

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

  jwtHelper: JwtHelper;

  constructor(private localStorageService: LocalStorageService,
              private route: ActivatedRoute,
              private router: Router,
              public colors: ColorsService,
              public requestsService: RequestsService) {
    this.jwtHelper = new JwtHelper();
    // get data from rest api
    console.log(requestsService.requests);
  }

  ngOnInit() {
    const localStorageService: LocalStorageService = this.localStorageService;
    const token: any = this.localStorageService.get('id_token');
    let isValidToken: boolean = false;

    try {
      isValidToken = !this.jwtHelper.isTokenExpired(token);
    } catch (e) {}

    if (!isValidToken) {
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
