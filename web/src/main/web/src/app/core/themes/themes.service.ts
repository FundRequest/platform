import {Injectable} from '@angular/core';

const themeFundRequest = require('../../shared/styles/themes/theme-fund-request.scss');

@Injectable()
export class ThemesService {

  styleTag: any;
  defaultTheme: string = 'FundRequest';

  constructor() {
    this.createStyle();
    this.setTheme();
  }

  private createStyle() {
    const head = document.head || document.getElementsByTagName('head')[0];
    this.styleTag = document.createElement('style');
    this.styleTag.type = 'text/css';
    this.styleTag.id = 'appthemes';
    head.appendChild(this.styleTag);
  }

  setTheme() {
    this.injectStylesheet(themeFundRequest);
  }

  injectStylesheet(css) {
    this.styleTag.innerHTML = css;
  }

  getDefaultTheme() {
    return this.defaultTheme;
  }
}
