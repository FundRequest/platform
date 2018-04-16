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

    public static loadOnPageReady(readyFunction) {
        $(() => readyFunction());
    }

    private static _handleHttpErrors(response) {
        if (!response.ok) {
            throw Error(response.statusText);
        }
        return response;
    }

    public static fetchJSON(url: string) {
        return fetch(url)
            .then(Utils._handleHttpErrors)
            .then(res => res.json())
            .catch(err => null);
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
                    isValid = isValid && (value.trim().length <= 0 || /^[0-9]+(\.[0-9]{1,2})?$/.exec(value) != null);
                    break;
                case 'github':
                    isValid = isValid && (value.trim().length <= 0 || (await Utils._validation.github(value)));
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

    private static _validation = {
        github: (link): Promise<boolean> => {
            return Github.validateLink(link);
        }
    };


}
