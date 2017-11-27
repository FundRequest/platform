import { Component, OnInit } from '@angular/core';
import { ToasterConfig } from 'angular2-toaster';

@Component({
  selector   : 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls  : ['./layout.component.scss']
})
export class LayoutComponent implements OnInit {

  public toasterConfig = new ToasterConfig({
    positionClass: 'toast-bottom-right',
    messageClass : 'toast-custom-message'
  });

  constructor() {
  }

  ngOnInit() {
  }


}
