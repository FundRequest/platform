/* tslint:disable:no-unused-variable */

import {inject, TestBed} from "@angular/core/testing";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {Http, HttpModule} from "@angular/http";

import {TranslatorService} from "./translator.service";
import {createTranslateLoader} from "../../app.module";

describe('Service: Translator', () => {
    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [
                HttpModule,
                TranslateModule.forRoot({
                    loader: {
                        provide: TranslateLoader,
                        useFactory: (createTranslateLoader),
                        deps: [Http]
                    }
                })
            ],
            providers: [TranslatorService]
        });
    });

    it('should ...', inject([TranslatorService], (service: TranslatorService) => {
        expect(service).toBeTruthy();
    }));
});
