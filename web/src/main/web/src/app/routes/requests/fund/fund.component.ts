import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {UserService} from '../../../services/user/user.service';
import {IUserRecord} from '../../../redux/user.models';
import {RequestService} from "../../../services/request/request.service";
import {Utils} from "../../../shared/utils";
import {FundRequestCommand} from "../../../redux/requests.models";
import {PlatformIdResolverService} from "../../../services/request/platformIdResolver.service";

import * as swal from 'sweetalert';

@Component({
  selector: 'fnd-request-detail',
  templateUrl: './fund.component.html',
  styleUrls: ['./fund.component.scss']
})
export class FundComponent implements OnInit {

  public user: IUserRecord;
  public requestDetails: any;
  public fundAmount: number;
  public balance: number;
  public fundingInProgress: boolean = false;
  public qrValue: string = '';

  constructor(private route: ActivatedRoute,
              private userService: UserService,
              private http: HttpClient,
              private requestService: RequestService,
              private platformIdResolverService: PlatformIdResolverService) {
  }

  ngOnInit(): void {
    this.userService.currentUser$.subscribe(user => this.user = user);
    this.route.queryParams.subscribe(params => {
      let issueLink = params['url'];
      let matches = /^https:\/\/github\.com\/(.+)\/(.+)\/issues\/(\d+)$/.exec(issueLink);
      if (matches && matches.length >= 4) {
        this.fillRequestDetails(matches, issueLink);
      } else {
        console.log("no url match");
      }
    });
    this.userService.currentUser$.subscribe((user: IUserRecord) => {
      this.user = user;
      this.balance = Utils.fromWeiRounded(user.balance);
    });
  }

  private fillRequestDetails(matches: RegExpExecArray, issueLink: string) {
    let url = 'https://api.github.com/repos/' + matches[1] + '/' + matches[2] + '/issues/' + matches[3];
    this.http.get(url).subscribe(data => {
      this.requestDetails = data;
      this.requestDetails.platform = 'GITHUB';
      this.requestDetails.platformId = this.platformIdResolverService.resolve('GITHUB', issueLink);
      this.requestDetails.issueNumber = matches[3];
      this.requestDetails.link = issueLink;
      this.requestDetails.repo = matches[1];
      this.requestDetails.owner = matches[2];
      let technologiesUrl = 'https://api.github.com/repos/' + matches[1] + '/' + matches[2] + '/languages';
      this.http.get(technologiesUrl).subscribe(data => {
        this.requestDetails.technologies = Object.keys(data);
      });
    });
  }

  public fund() {
    this.fundingInProgress = true;
    this.requestService.fundRequest(
      new FundRequestCommand(
        this.requestDetails.platform,
        this.requestDetails.platformId,
        this.requestDetails.link,
        this.fundAmount)
    ).catch((e) => {
      swal('Error when funding', 'An error ocurred wile funding: ' + e.message, 'error');
      this.fundingInProgress = false;
    });
  }

  public updateQr() {
    this.requestService.requestQRValue(new FundRequestCommand(
      this.requestDetails.platform,
      this.requestDetails.platformId,
      this.requestDetails.link,
      this.fundAmount
    )).then(
      res => { // Success
        this.qrValue = res;
      },
      msg => { // Error
      }
    ).catch();
  }

}
