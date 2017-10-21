import {Component, Input} from '@angular/core';
import {Request} from '../../core/requests/Request';

@Component({
  selector: 'app-watchlink',
  templateUrl: './watchlink.component.html',
  styleUrls: ['./watchlink.component.scss']
})
export class WatchlinkComponent {
  @Input() request: Request = new Request();
}
