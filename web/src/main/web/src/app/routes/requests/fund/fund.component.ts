import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {RequestService} from '../../../services/request/request.service';
import {Utils} from '../../../shared/utils';
import {FundRequestCommand} from '../../../redux/requests.models';
import {AccountWeb3Service} from '../../../services/accountWeb3/account-web3.service';
import {IAccountWeb3Record} from '../../../redux/accountWeb3.models';
import swal from 'sweetalert2';

@Component({
  selector: 'fnd-request-detail',
  templateUrl: './fund.component.html',
  styleUrls: ['./fund.component.scss']
})
export class FundComponent implements OnInit {

  private _accountWeb3: IAccountWeb3Record;
  public requestDetails: any;
  public fundAmount: number;
  public balance: number;
  public fundingInProgress: boolean = false;
  public qrValue: string = '';

  constructor(private route: ActivatedRoute,
    private _aw3s: AccountWeb3Service,
    private http: HttpClient,
    private requestService: RequestService) {
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      let issueLink = params['url'];
      let matches = /^https:\/\/github\.com\/(.+)\/(.+)\/issues\/(\d+)$/.exec(issueLink);
      if (matches && matches.length >= 4) {
        this.fillRequestDetails(matches, issueLink);
      } else {
        console.log('no url match');
      }
    });
    this._aw3s.currentAccountWeb3$.subscribe((accountWeb3: IAccountWeb3Record) => {
      this._accountWeb3 = accountWeb3;
      this.balance = Utils.fromWeiRounded(this._accountWeb3.balance);
    });
  }

  private fillRequestDetails(matches: RegExpExecArray, issueLink: string) {
    let url = `https://api.github.com/repos/${matches[1]}/${matches[2]}/issues/${matches[3]}`;
    this.http.get(url).subscribe(data => {
      this.requestDetails = data;
      this.requestDetails.platform = 'GITHUB';
      this.requestDetails.platformId = Utils.getPlatformIdFromUrl(issueLink);
      this.requestDetails.issueNumber = matches[3];
      this.requestDetails.link = issueLink;
      this.requestDetails.repo = matches[1];
      this.requestDetails.owner = matches[2];
      let technologiesUrl = `https://api.github.com/repos/${matches[1]}/${matches[2]}/languages`;
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
        this.fundAmount)
    ).catch((e) => {
      swal({
        title: 'Error when funding',
        text: `An error ocurred wile funding: ${e.message}`,
        type: 'error'
      });
      this.fundingInProgress = false;
    });
  }

  public updateQr() {
    this.requestService.requestQRValue(new FundRequestCommand(
      this.requestDetails.platform,
      this.requestDetails.platformId,
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
