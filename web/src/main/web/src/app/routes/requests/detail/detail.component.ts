import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ClaimRequestCommand, IRequestRecord} from '../../../redux/requests.models';
import {RequestService} from '../../../services/request/request.service';
import {UserService} from '../../../services/user/user.service';
import {IUserRecord} from '../../../redux/user.models';
import {AuthService} from "../../../core/auth/auth.service";


const swal = require('sweetalert');

@Component({
  selector   : 'fnd-request-detail',
  templateUrl: './detail.component.html',
  styleUrls  : ['./detail.component.scss']
})
export class DetailComponent implements OnInit {

  public user: IUserRecord;
  public request: IRequestRecord;

  constructor(private route: ActivatedRoute,
              private requestService: RequestService,
              private userService: UserService,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(user => this.user = user);
    this.route.params.subscribe(params => {
      let id = +params['id'];
      this.requestService
        .requests$.map(list => list.filter(request => request.id == id).first())
        .subscribe(request => {
          this.request = request;
        });
    });
  }

  claim(): void {
    if(this.authService.isAuthenticated()) {
      let info = this.request.issueInformation;
      this.requestService.claimRequest(new ClaimRequestCommand(this.request.id, info.platform, info.platformId, ''));
    } else {
      swal('Not authenticated',
        'For claiming a request you need to be logged in with an account that is linked to Github.', 'error'
      );
    }
  }
}
