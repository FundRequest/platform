import {Alert} from "./alert";
import {Utils} from './Utils';

export class InstantEdit {
    private _document: HTMLDocument = document;
    private _invalidFormClass: string = 'is-invalid';
    private _invalidMessageClass: string = 'invalid-feedback';

    constructor() {
        let list = this._document.querySelectorAll('[data-edit]');
        for (let i = 0; i < list.length; i++) {
            let li: HTMLInputElement = list.item(i) as HTMLInputElement;
            let name = li.dataset.edit;
            li.addEventListener('focusin', () => {
                li.classList.remove(this._invalidFormClass);
            });
            li.addEventListener('focusout', () => {
                this._saveItem(li, name);
            });
        }
    }

    private _validateItem(field, name) {
        let valid = true;
        let message = '';
        let value = field.value;
        let validationValue = field.dataset.editValidation;
        let validations = validationValue.split(',');

        for (let i = 0; i < validations.length && valid; i++) {
            let validation = validations[i];
            switch (validation) {
                case 'required':
                    valid = value.length;
                    message = 'Field is required';
                    break;
                case 'ethereum':
                    valid = value.length > 0 ? /^0x[a-fA-F0-9]{40}$/.test(value) : true;
                    message = 'Not a valid ethereum address';
                    break;
                case 'telegram-handle':
                    valid = value.length > 0 ? /^@?[a-zA-Z][a-zA-Z0-9_]*[a-zA-Z0-9]$/.test(value) : false;
                    message = 'Telegram handle is required, you can use a-z, 0-9 and underscores.';
                    break;
            }
        }

        if (!valid) {
            this._showError(field, name, message);
        } else {
            this._hideError(field, name);
        }

        return valid;
    }

    private _saveItem(field, name) {
        let isValid = this._validateItem(field, name);
        let self = this;
        let title = null;
        let postAddress = null;
        let data = null;

        if (!isValid) {
            return;
        }

        Utils.showLoading();

        switch (field.dataset.edit) {
            case 'eth-address':
                title = 'ETH address';
                postAddress = '/profile/etheraddress';
                data = {etheraddress: field.value};
                break;
            case 'telegram-handle':
                title = 'Telegram handle';
                postAddress = '/profile/telegramname';
                data = {telegramname: field.value};
                break;
            default:
                Utils.hideLoading();
                return;
        }

        $.post(postAddress, data).promise()
            .then(() => {
                self._hideError(field, name);
                Alert.show(`${title} saved!`);
            })
            .catch(() => {
                self._showError(field, name, 'Something went wrong.');
            })
            .always(() => {
                Utils.hideLoading();
                self._showHideEmptyMessage(field, name);
            });
    }

    private _hideError(field, name) {
        this._showHideError(field, name);
    }

    private _showError(field, name, message) {
        this._showHideError(field, name, true, message);
    }

    private _showHideError(field, name, show = false, errorMessage = '') {
        let messageField: HTMLElement = this._document.querySelector(`[data-edit-messages="${name}"]`) as HTMLElement;
        show ? messageField.classList.add(this._invalidMessageClass) : messageField.classList.remove(this._invalidMessageClass);
        show ? messageField.innerHTML = errorMessage : messageField.innerHTML = '';
        show ? field.classList.add(this._invalidFormClass) : field.classList.remove(this._invalidFormClass);
    }

    private _showHideEmptyMessage(field, name) {
        let messageField: HTMLElement = this._document.querySelector(`[data-edit-empty-message="${name}"]`) as HTMLElement;
        if (messageField != null) {
            field.value && field.value.trim().length > 0 ? messageField.classList.add('d-none') : messageField.classList.remove('d-none');
        }
    }
}
