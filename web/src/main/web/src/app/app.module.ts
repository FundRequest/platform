import {BrowserAnimationsModule} from "@angular/platform-browser/animations"; // this is needed!
import {NgModule} from "@angular/core";
import {Http, HttpModule} from "@angular/http";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";

import {AppComponent} from "./app.component";

import {FormsModule} from '@angular/forms';
import {CoreModule} from "./core/core.module";
import {LayoutModule} from "./layout/layout.module";
import {SharedModule} from "./shared/shared.module";
import {RoutesModule} from "./routes/routes.module";
import {ActionReducer, ActionReducerMap, MetaReducer, StoreModule} from '@ngrx/store';
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {AuthInterceptor} from "./core/auth/auth.interceptor";
import {ComponentsModule} from "./components/components.module";
import {ModalModule} from "ngx-bootstrap";
import {EmptyResponseBodyErrorInterceptor} from "./core/empty-response-body-error/empty-response-body-error.interceptor";
import {IState, REDUCER_MAP} from "./redux/store";
import {localStorageSync} from "ngrx-store-localstorage";

// https://github.com/ocombe/ng2-translate/issues/218
export function createTranslateLoader(http: Http) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}


const reducers: ActionReducerMap<IState> = REDUCER_MAP;

export function localStorageSyncReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  return localStorageSync({keys: ['requests']})(reducer);
}
const metaReducers: Array<MetaReducer<any, any>> = [localStorageSyncReducer];

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    HttpModule,
    ComponentsModule,
    BrowserAnimationsModule, // required for ng2-tag-input
    CoreModule,
    LayoutModule,
    SharedModule.forRoot(),
    FormsModule,
    RoutesModule,
    StoreModule.forRoot(
      reducers,
      {metaReducers}
    ),
    ModalModule.forRoot(),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [Http]
      }
    }),
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: EmptyResponseBodyErrorInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
