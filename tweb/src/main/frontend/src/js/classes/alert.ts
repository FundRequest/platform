import * as toastr from 'toastr';
import 'toastr/toastr.scss';

export class Alert {
    // private static readonly _container: HTMLElement = document.getElementById('alert-container') as HTMLElement;
    // private static readonly _options: any = {
    //     type: 'success',
    //     timeout: 3000
    // };

    public static success(message: string): void {
        Alert.show(message, 'success');
    }

    public static warning(message: string): void {
        Alert.show(message, 'warning');
    }

    public static error(message: string): void {
        Alert.show(message, 'error');
    }

    public static info(message: string): void {
        Alert.show(message, 'info');
    }

    public static show(message: string, type: ToastrType = 'success'): void {
        toastr.options.closeButton = false;
        toastr.options.debug = false;
        toastr.options.progressBar = true;
        toastr.options.positionClass = 'toast-top-center';
        toastr.options.preventDuplicates = false;
        toastr.options.onclick = null;
        toastr.options.showDuration = 300;
        toastr.options.hideDuration = 1000;
        toastr.options.timeOut = 5000;
        toastr.options.extendedTimeOut = 1000;
        toastr.options.showEasing = 'swing';
        toastr.options.hideEasing = 'linear';
        toastr.options.showMethod = 'fadeIn';
        toastr.options.hideMethod = 'fadeOut';
        //toastr.options.toastClass = 'alert';
        //toastr.options.iconClasses = {
        //    error: 'alert-error',
        //    info: 'alert-info',
        //    success: 'alert-success',
        //    warning: 'alert-warning'
        //};

        switch (type) {
            case 'error':
                toastr.error(message);
                break;
            case 'warning':
                toastr.warning(message);
                break;
            case 'info':
                toastr.info(message);
                break;
            default:
                toastr.success(message);
                break;
        }
    }

    /*
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
        }*/
}