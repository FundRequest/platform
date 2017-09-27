import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl, ValidatorFn } from '@angular/forms';
import { CustomValidators } from 'ng2-validation';

@Component({
    selector: 'app-validation',
    templateUrl: './validation.component.html',
    styleUrls: ['./validation.component.scss']
})
export class ValidationComponent implements OnInit {

    valForm: FormGroup;
    blackList = ['bad@email.com', 'some@mail.com', 'wrong@email.co'];

    constructor(fb: FormBuilder) {

        let password = new FormControl('', Validators.required);
        let certainPassword = new FormControl('', CustomValidators.equalTo(password));

        // Model Driven validation
        this.valForm = fb.group({

            'sometext': [null, Validators.required],
            'checkbox': [null, Validators.required],
            'radio': ['', Validators.required],
            'select': [null, Validators.required],
            'digits': [null, Validators.pattern('^[0-9]+$')],
            'minlen': [null, Validators.minLength(6)],
            'maxlen': [null, Validators.maxLength(10)],

            'email': [null, CustomValidators.email],
            'url': [null, CustomValidators.url],
            'date': [null, CustomValidators.date],
            'number': [null, Validators.compose([Validators.required, CustomValidators.number])],
            'alphanum': [null, Validators.pattern('^[a-zA-Z]+$')],
            'minvalue': [null, CustomValidators.min(6)],
            'maxvalue': [null, CustomValidators.max(10)],
            'minwords': [null, this.minWords(6)],
            'maxwords': [null, this.maxWords(10)],
            'minmaxlen': [null, CustomValidators.rangeLength([6, 10])],
            'range': [null, CustomValidators.range([10, 20])],
            'rangewords': [null, Validators.compose([this.minWords(6), this.maxWords(10)])],
            'email_bl': [null, this.checkBlackList(this.blackList) ],

            'passwordGroup': fb.group({
                password: password,
                confirmPassword: certainPassword
            })

        });
    }

    submitForm($ev, value: any) {
        $ev.preventDefault();
        for (let c in this.valForm.controls) {
            this.valForm.controls[c].markAsTouched();
        }
        if (this.valForm.valid) {
            console.log('Valid!');
        }
        console.log(value);
    }

    minWords(checkValue): ValidatorFn {
        return <ValidatorFn>((control: FormControl) => {
            return (control.value || '').split(' ').length >= checkValue ? null : { 'minWords': control.value };
        });
    }

    maxWords(checkValue): ValidatorFn {
        return <ValidatorFn>((control: FormControl) => {
            return (control.value || '').split(' ').length <= checkValue ? null : { 'maxWords': control.value };
        });
    }

    checkBlackList(list: Array<string>): ValidatorFn {
        return <ValidatorFn>((control: FormControl) => {
            return list.indexOf(control.value) < 0 ? null : { 'blacklist': control.value };
        });
    }

    ngOnInit() {
    }

}
