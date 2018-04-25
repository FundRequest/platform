import * as $ from 'jquery';

export class Alert {
    private static readonly _container: HTMLElement = document.getElementById('alert-container') as HTMLElement;
    private static readonly _options: any = {
        type: 'success',
        timeout: 3000
    };

    public static show(message: string, type: string = ''): void {
        let optionType = type ? type : this._options.type;
        let newAlert = this._getAlertElement();
        let innerNewAlert = newAlert.querySelector('.alert-content');
        innerNewAlert ? innerNewAlert.innerHTML = message : null;
        newAlert.classList.add(`alert-${optionType}`);

        while (this._container.children.length > 3) {
            this._container.removeChild(this._container.lastChild);
        }
        this._container.insertBefore(newAlert, this._container.firstChild);
        setTimeout(function () {
            newAlert.classList.add('show');
        }, 100);

        setTimeout(function () {
            $(newAlert).alert('close'); // need jquery because it used bs
        }, this._options.timeout);
    }

    private static _getAlertElement(): HTMLElement {
        let element = document.createElement('div');
        element.classList.add('alert', 'alert-dismissible', 'fade');
        element.innerHTML = '<span class="alert-content"></span><button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>';
        return element;
    }
}
