import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ClaimRequestCommand, IRequestRecord} from '../../../redux/requests.models';
import {RequestService} from '../../../services/request/request.service';
import {UserService} from '../../../services/user/user.service';
import {IUserRecord} from '../../../redux/user.models';
import {AuthService} from "../../../core/auth/auth.service";
import {Subscription} from 'rxjs/Subscription';

import * as swal from 'sweetalert';
import {Utils} from '../../../shared/utils';

@Component({
  selector   : 'fnd-request-detail',
  templateUrl: './detail.component.html',
  styleUrls  : ['./detail.component.scss']
})
export class DetailComponent implements OnInit, OnDestroy {

  public user: IUserRecord;
  public request: IRequestRecord;

  public requestSubscription: Subscription;

  constructor(private route: ActivatedRoute,
              private _rs: RequestService,
              private _us: UserService,
              private _as: AuthService) {
  }

  ngOnInit(): void {
    this._us.currentUser$.subscribe(user => this.user = user);
    this.route.params.subscribe(params => {
      let id = +params['id'];
      this.requestSubscription = this._rs.requests$.map(list => list.filter(request => request.id == id).first())
        .subscribe(request => {
          this.request = request;
        });
    });
  }

  public getLink(request: IRequestRecord): string {
    return Utils.getUrlFromId(request.issueInformation.platform, request.issueInformation.platformId);
  }

  public get claimed(): boolean {
    return typeof this.request != 'undefined' && this.request.status == 'CLAIMED';
  }

  public claim(): void {
    if(this._as.isAuthenticated()) {
      let info = this.request.issueInformation;
      this._rs.claimRequest(new ClaimRequestCommand(this.request.id, info.platform, info.platformId, ''));
    } else {
      swal('Not authenticated',
        'For claiming a request you need to be logged in with an account that is linked to Github.', 'error'
      );
    }
  }

  ngOnDestroy() {
    this.requestSubscription ? this.requestSubscription.unsubscribe() : null;
  }
}
