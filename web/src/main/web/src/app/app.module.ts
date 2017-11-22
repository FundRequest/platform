import { BrowserAnimationsModule } from '@angular/platform-browser/animations'; // this is needed!
import { NgModule, APP_INITIALIZER } from '@angular/core';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslateService, TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { AppComponent } from './app.component';

import { CoreModule } from './core/core.module';
import { LayoutModule } from './layout/layout.module';
import { SharedModule } from './shared/shared.module';
import { RoutesModule } from './routes/routes.module';

import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from './core/auth/auth.interceptor';
import { ComponentsModule } from './components/components.module';
import { ModalModule } from 'ngx-bootstrap';

import { EmptyResponseBodyErrorInterceptor } from './core/empty-response-body-error/empty-response-body-error.interceptor';

import { ServiceModule } from './services/service.module';

import { REDUCER_MAP } from './redux/store';
import { StoreModule } from '@ngrx/store';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';

// https://github.com/ocombe/ng2-translate/issues/218
export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports     : [
    HttpClientModule,
    BrowserAnimationsModule, // required for ng2-tag-input
    CoreModule,
    LayoutModule,
    SharedModule.forRoot(),
    RoutesModule,
    TranslateModule.forRoot({
      loader: {
        provide   : TranslateLoader,
        useFactory: (createTranslateLoader),
        deps      : [HttpClient]
      }
    }),
    ModalModule.forRoot(),
    FormsModule,
    ComponentsModule,
    ServiceModule,
    StoreModule.forRoot(REDUCER_MAP),
    // Note that you must instrument after importing StoreModule
    StoreDevtoolsModule.instrument({
      maxAge: 25 //  Retains last 25 states
    })
  ],
  providers   : [
    {
      provide : HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi   : true
    },
    {
      provide : HTTP_INTERCEPTORS,
      useClass: EmptyResponseBodyErrorInterceptor,
      multi   : true
    },
    /*{ // Function that is called before the app loads
     provide: APP_INITIALIZER,
     useFactory: (cs: ContractsService) => function() {return cs.init()},
     deps: [ContractsService],
     multi: true
     }*/
  ],
  bootstrap   : [AppComponent]
})
export class AppModule {
}
