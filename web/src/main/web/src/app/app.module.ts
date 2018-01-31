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
import { MomentModule } from 'angular2-moment';
import { ContractsService } from './services/contracts/contracts.service';
import { AppFactory } from './app.factory';
import {AccountWeb3Service} from './services/accountWeb3/account-web3.service';

// https://github.com/ocombe/ng2-translate/issues/218
export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports     : [
    BrowserAnimationsModule, // required for ng2-tag-input
    CoreModule,
    ComponentsModule,
    FormsModule,
    HttpClientModule,
    LayoutModule,
    ModalModule.forRoot(),
    MomentModule,
    RoutesModule,
    SharedModule.forRoot(),
    TranslateModule.forRoot({
      loader: {
        provide   : TranslateLoader,
        useFactory: (createTranslateLoader),
        deps      : [HttpClient]
      }
    }),
    ServiceModule,
    StoreModule.forRoot(REDUCER_MAP),
    // Note that you must instrument after importing StoreModule
    StoreDevtoolsModule.instrument({
      maxAge: 25 //  Retains last 25 states
    })
  ],
  providers   : [AppFactory, ContractsService, AccountWeb3Service,
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
    {
      provide   : APP_INITIALIZER,
      useFactory: (appInitializer: AppFactory) => () => appInitializer.load(),
      deps      : [AppFactory, ContractsService, AccountWeb3Service],
      multi     : true
    }
  ],
  bootstrap   : [AppComponent]
})

export class AppModule {
}
