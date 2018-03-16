import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ClaimRequestCommand, IRequestRecord, RequestStatus} from '../../../redux/requests.models';
import {RequestService} from '../../../services/request/request.service';
import {UserService} from '../../../services/user/user.service';
import {IUserRecord} from '../../../redux/user.models';
import {Utils} from '../../../shared/utils';
import {AuthService} from '../../../core/auth/auth.service';
import {Subscription} from 'rxjs/Subscription';
import swal from 'sweetalert2';

@Component({
  selector: 'fnd-request-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit, OnDestroy {

  public user: IUserRecord;
  public request: IRequestRecord;

  private _requestSubscription: Subscription;
  private _routeSubscription: Subscription;
  private _userSubscription: Subscription;

  private _canClaim: boolean = null;
  private _showClaim: boolean = null;

  constructor(private route: ActivatedRoute,
    private _rs: RequestService,
    private _us: UserService,
    private _as: AuthService) {
  }

  async ngOnInit() {
    this._userSubscription = this._us.currentUser$.subscribe(user => this.user = user);
    this._routeSubscription = this.route.params.subscribe(params => {
      let id = +params['id'];
      this._requestSubscription = this._rs.requests$.map(list => list.filter(request => request.id == id).first())
        .subscribe(request => {
          this.request = request;
        });
    });
  }

  public getLink(request: IRequestRecord): string {
    return Utils.getUrlFromId(request.issueInformation.platform, request.issueInformation.platformId);
  }

  public get claimed(): boolean {
    return typeof this.request != 'undefined' && this.request.status == RequestStatus.CLAIMED;
  }

  public get showClaimButton(): boolean {
    return !this.claimed;
  }

  public async canClaim(): Promise<boolean> {
    if (this._canClaim == null) {
      this._canClaim = this._as.isAuthenticated() && await this._rs.canClaim(this.request);
    }

    return this._canClaim;
  }

  public async claim(): Promise<void> {
    let canClaim = await this.canClaim();
    if (canClaim) {
      let info = this.request.issueInformation;
      this._rs.claimRequest(new ClaimRequestCommand(this.request.id, info.platform, info.platformId, ''));
    } else {
      swal({
        title: 'Not authenticated',
        text: 'For claiming a request you need to be logged in with an account that is linked to Github. And you need to be the solver of this issue.',
        type: 'error'
      });
    }
  }

  ngOnDestroy() {
    this._requestSubscription ? this._requestSubscription.unsubscribe() : null;
    this._routeSubscription ? this._routeSubscription.unsubscribe() : null;
    this._userSubscription ? this._userSubscription.unsubscribe() : null;
  }
}
