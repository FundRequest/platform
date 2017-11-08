import {Component, OnInit} from "@angular/core";
import {ActivatedRoute} from "@angular/router";
import {Subscription} from "rxjs/Subscription";
import {IRequestRecord} from "../../../redux/requests.models";
import {RequestService} from "../../../services/request/request.service";
import {ContractsService} from "../../../services/contracts/contracts.service";
import {UserService} from "../../../services/user/user.service";
import {IUserRecord} from "../../../redux/user.models";

@Component({
  selector: 'fnd-request-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {

  public user: IUserRecord;
  public request: IRequestRecord;

  constructor(private route: ActivatedRoute,
              private requestService: RequestService,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.userService.getCurrentUser().subscribe(user => this.user = user);
    this.route.params.subscribe(params => {
      let id = +params['id'];
      this.requestService
        .requests.map(list => list.filter(request => request.id == id).first())
        .subscribe(request => {
          this.request = request;
        });
    });
  }
}
