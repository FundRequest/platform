import * as $ from 'jquery';
import {Github} from './github';

export class Utils {

    public static showLoading() {
        let loader = document.querySelector('[data-page-loader]');
        loader ? loader.classList.remove('d-none') : null;
    }

    public static hideLoading() {
        let loader = document.querySelector('[data-page-loader]');
        loader ? loader.classList.add('d-none') : null;
    }

    public static getLocationHashValue() {
        return window.location.hash ? window.location.hash.split('#')[1] : '';
    }

    public static getNewWindow(url, widthPopup, heightPopup) {
        let left = (screen.width / 2) - (widthPopup / 2);
        let top = (screen.height / 2) - (heightPopup / 2);
        let newWindow = window.open('', 'popup', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width=' + widthPopup + ', height=' + heightPopup + ', top=' + top + ', left=' + left);
        if (newWindow) {
            newWindow.opener = null;
            newWindow.location.assign(url);
        }

        return newWindow;
    }

    public static formatTokenPrice(value, decimals: number = 2) {
        if (value) {
            let val = (value / 1).toFixed(decimals);
            return val.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
        } else {
            return value;
        }
    }

    public static arrayContainsRegex(stringArray: string[], regex: RegExp): boolean {
        for (let i = 0; i < stringArray.length; i++) {
            if (stringArray[i].match(regex)) return true;
        }
        return false;
    }

    public static loadOnPageReady(readyFunction) {
        $(() => readyFunction());
    }

    private static _handleHttpErrors(response) {
        if (!response.ok) {
            throw Error(response.statusText);
        }
        return response;
    }

    public static fetchJSON(url: string, body: any = null): Promise<any> {
        if (body == null) {
            return fetch(url, {credentials: 'same-origin'})
                .then(Utils._handleHttpErrors)
                .then(res => res ? res.json() : null)
                .catch(err => null);
        } else {
            return fetch(url, {
                method: 'POST',
                body: JSON.stringify(body),
                credentials: 'same-origin',
                headers: new Headers({
                    'Content-Type': 'application/json'
                })
            }).then(res => res ? res.json() : null
            ).catch(err => null);
        }
    }

    public static fetchHTML(url: string, body: any = null): Promise<any> {
        if (body == null) {
            return fetch(url, {credentials: 'same-origin'})
                .then(Utils._handleHttpErrors)
                .then(res => res ? res.text() : null)
                .catch(err => null);
        } else {
            return fetch(url, {
                method: 'POST',
                body: JSON.stringify(body),
                credentials: 'same-origin',
                headers: new Headers({
                    'Content-Type': 'application/json'
                })
            }).then(res => res ? res.text() : null
            ).catch(err => null);
        }
    }

    public static async validateHTMLElement(element: HTMLElement, validations: string[], callback = null): Promise<boolean> {
        let isValid: boolean = true;
        let value = '';

        switch (element.tagName.toLowerCase()) {
            case 'input':
                value = (<HTMLInputElement>element).value;
                break;
            case 'textarea':
                value = (<HTMLTextAreaElement>element).value;
                break;
            case 'select':
                value = (<HTMLSelectElement>element).value;
                break;
        }

        for (let validation of validations) {
            switch (validation) {
                case 'required':
                    isValid = isValid && value.trim().length > 0;
                    break;
                case 'number':
                    isValid = isValid && (value.trim().length <= 0 || Utils.validators.number(value));
                    break;
                case 'github':
                    isValid = isValid && (value.trim().length <= 0 || (await Utils.validators.github(value)));
                    break;
            }
        }

        if (isValid) {
            element.classList.add('is-valid');
            element.classList.remove('is-invalid');
            if (callback != null) {
                await callback(value);
            }
        } else {
            element.classList.remove('is-valid');
            element.classList.add('is-invalid');
        }

        return isValid;
    }

    public static modal = {
        open: (el: HTMLElement, closeCallback) => {
            document.body.classList.add('modal-open');
            document.body.style.overflow = 'hidden';
            el.style.display = 'block';
            setTimeout(() => {
                el.classList.add('show');
            }, 150);

            document.body.addEventListener('click', (e) => {
                if (e.target == el) {
                    Utils.modal.close(el);
                    closeCallback();
                }
            });
        },
        close: (el: HTMLElement) => {
            document.body.classList.remove('modal-open');
            document.body.style.overflow = '';
            el.classList.remove('show');
            setTimeout(() => {
                el.style.display = 'none';
            }, 150);
        }
    };

    public static validators = {
        github: (link): Promise<boolean> => {
            return Github.validateLink(link);
        },
        number: (value) => {
            return /^[0-9]+(\.[0-9]{1,2})?$/.exec(value.trim()) != null;
        }
    };


}
