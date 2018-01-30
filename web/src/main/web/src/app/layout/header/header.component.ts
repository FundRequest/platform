import { Component, OnInit, ViewChild } from '@angular/core';
import { UserblockService } from '../sidebar/userblock/userblock.service';
import { SettingsService } from '../../core/settings/settings.service';
import { MenuService } from '../../core/menu/menu.service';
import { UserService } from '../../services/user/user.service';
import {Observable} from 'rxjs/Observable';
import {ContractsService} from '../../services/contracts/contracts.service';

declare let require: any;
declare let $: any;

const screenfull = require('screenfull');
const browser = require('jquery.browser');

@Component({
  selector   : 'app-header',
  templateUrl: './header.component.html',
  styleUrls  : ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  navCollapsed = true; // for horizontal layout
  menuItems = []; // for horizontal layout

  isNavSearchVisible: boolean;
  @ViewChild('fsbutton') fsbutton;  // the fullscreen button

  constructor(public menu: MenuService,
              public userblockService: UserblockService,
              public settings: SettingsService,
              public userService: UserService,
              private _cs: ContractsService) {
    this.menuItems = menu.getMenu().slice(0, 4); // for horizontal layout
  }

  ngOnInit() {
    this.isNavSearchVisible = false;
    if (browser.msie) { // Not supported under IE
      this.fsbutton.nativeElement.style.display = 'none';
    }
  }

  public get locked$(): Observable<boolean> {
      return this._cs.locked$;
  }

  public get supported$(): Observable<boolean> {
      return this._cs.supported$;
  }

  public get network$(): Observable<string> {
    return this._cs.network$;
  }

  logout($event) {
    $event.preventDefault();
    this.userService.logout();
  }

  toggleUserBlock($event) {
    $event.preventDefault();
    this.userblockService.toggleVisibility();
  }

  openNavSearch($event) {
    $event.preventDefault();
    $event.stopPropagation();
    this.setNavSearchVisible(true);
  }

  setNavSearchVisible(stat: boolean) {
    this.isNavSearchVisible = stat;
  }

  getNavSearchVisible() {
    return this.isNavSearchVisible;
  }

  toggleOffsidebar() {
    this.settings.layout.offsidebarOpen = !this.settings.layout.offsidebarOpen;
  }

  toggleCollapsedSideabar() {
    this.settings.layout.isCollapsed = !this.settings.layout.isCollapsed;
  }

  isCollapsedText() {
    return this.settings.layout.isCollapsedText;
  }

  toggleFullScreen(event) {

    if (screenfull.enabled) {
      screenfull.toggle();
    }
    // Switch icon indicator
    let el = $(this.fsbutton.nativeElement);
    if (screenfull.isFullscreen) {
      el.children('em').removeClass('fa-expand').addClass('fa-compress');
    }
    else {
      el.children('em').removeClass('fa-compress').addClass('fa-expand');
    }
  }
}
