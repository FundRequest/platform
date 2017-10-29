import {ModuleWithProviders, NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {TranslateModule} from "@ngx-translate/core";

import {ButtonsModule} from "ngx-bootstrap/buttons";
import {CollapseModule} from "ngx-bootstrap/collapse";
import {BsDropdownModule} from "ngx-bootstrap/dropdown";
import {ModalModule} from "ngx-bootstrap/modal";
import {PaginationModule} from "ngx-bootstrap/pagination";
import {ProgressbarModule} from "ngx-bootstrap/progressbar";
import {RatingModule} from "ngx-bootstrap/rating";
import {TabsModule} from "ngx-bootstrap/tabs";
import {TimepickerModule} from "ngx-bootstrap/timepicker";
import {TooltipModule} from "ngx-bootstrap/tooltip";
import {TypeaheadModule} from "ngx-bootstrap/typeahead";
import {DatepickerModule} from "ngx-bootstrap/datepicker";

import {SparklineDirective} from "./directives/sparkline/sparkline.directive";
import {EasypiechartDirective} from "./directives/easypiechart/easypiechart.directive";
import {ColorsService} from "./colors/colors.service";
import {CheckallDirective} from "./directives/checkall/checkall.directive";
import {NowDirective} from "./directives/now/now.directive";
import {ScrollableDirective} from "./directives/scrollable/scrollable.directive";
import {JqcloudDirective} from "./directives/jqcloud/jqcloud.directive";

import {LocalStorageModule} from "angular-2-local-storage";
import {FundButtonDirective} from "./directives/fund-button/fund-button.directive";

// https://angular.io/styleguide#!#04-10
@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    TranslateModule,
    ButtonsModule.forRoot(),
    CollapseModule.forRoot(),
    DatepickerModule.forRoot(),
    BsDropdownModule.forRoot(),
    ModalModule.forRoot(),
    PaginationModule.forRoot(),
    ProgressbarModule.forRoot(),
    RatingModule.forRoot(),
    TabsModule.forRoot(),
    TimepickerModule.forRoot(),
    TooltipModule.forRoot(),
    TypeaheadModule.forRoot(),
    LocalStorageModule.withConfig({
      prefix: 'FND',
      storageType: 'localStorage'
    })
  ],
  providers: [
    ColorsService
  ],
  declarations: [
    SparklineDirective,
    EasypiechartDirective,
    CheckallDirective,
    NowDirective,
    ScrollableDirective,
    JqcloudDirective,
    FundButtonDirective
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    TranslateModule,
    RouterModule,
    ButtonsModule,
    CollapseModule,
    DatepickerModule,
    BsDropdownModule,
    ModalModule,
    PaginationModule,
    ProgressbarModule,
    RatingModule,
    TabsModule,
    TimepickerModule,
    TooltipModule,
    TypeaheadModule,
    SparklineDirective,
    EasypiechartDirective,
    CheckallDirective,
    NowDirective,
    ScrollableDirective,
    JqcloudDirective,
    FundButtonDirective
  ]
})

// https://github.com/ocombe/ng2-translate/issues/209
export class SharedModule {
  static forRoot(): ModuleWithProviders {
    return {
      ngModule: SharedModule
    };
  }
}
