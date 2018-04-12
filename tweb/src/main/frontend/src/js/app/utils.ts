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

    public static getFundRequestTokenAddress() {
        return document.head.querySelector('[property="contracts:FundRequestToken"]').getAttribute('content');
    }

    public static loadOnPageReady(readyFunction) {
        if (document.readyState === 'complete') {
            readyFunction();
        } else {
            document.addEventListener('DOMContentLoaded', () => {
                readyFunction();
            });
        }
    }

    public static validateHTMLElement(element: HTMLElement, callback = null): boolean {
        let isValid: boolean = false;
        let validation = element.dataset.formValidation;
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

        switch (validation) {
            case 'required':
                isValid = value.trim().length > 0;
                break;
            case 'github':
                isValid = Utils._validation.github(value);
                break;
            default:
                isValid = true;
                break;
        }

        if (isValid) {
            element.classList.add('is-valid');
            element.classList.remove('is-invalid');
            if (callback != null) {
                callback(value);
            }
        } else {
            element.classList.remove('is-valid');
            element.classList.add('is-invalid');
        }

        return isValid;
    }

    private static _validation = {
        github: (link) => {
            return Github.validateLink(link);
        }
    };
}
