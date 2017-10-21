import {Component, OnInit} from "@angular/core";
import {RequestsService} from "../../../core/requests/requests.service";
import {Request} from "../../../core/requests/Request";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-request-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.scss']
})
export class DetailComponent implements OnInit {

  private subRoute;
  private subRequest;

  public id;

  public request: Request =  new Request();

  constructor(private route: ActivatedRoute, private requestsService: RequestsService) {
  }

  ngOnInit(): void {
    this.subRoute = this.route.params.subscribe(params => {
      this.id = +params['id'];

      this.subRequest = this.requestsService.get(this.id).subscribe((request) => {
          this.request.fillFromJSON(request);
        }
      );
    });
  }

  ngOnDestroy(): void {
      this.subRequest.unsubscribe();
      this.subRoute.unsubscribe();
  }
}
