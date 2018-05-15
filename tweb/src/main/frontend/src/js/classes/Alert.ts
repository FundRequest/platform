import * as toastr from 'toastr';

export type AlertType = ToastrType;
export type AlertPosition = 'top-left'|'top-center'|'top-right'|'bottom-left'|'bottom-center'|'bottom-right';

export default class Alert {
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

    public static _setToasterOptions(position: AlertPosition) {
        toastr.options.containerId = 'toast-container';
        toastr.options.closeButton = true;
        toastr.options.debug = false;
        toastr.options.progressBar = true;
        toastr.options.preventDuplicates = true;
        toastr.options.onclick = null;
        toastr.options.showDuration = 300;
        toastr.options.hideDuration = 1000;
        toastr.options.timeOut = 5000;
        toastr.options.extendedTimeOut = 1000;
        toastr.options.showEasing = 'swing';
        toastr.options.hideEasing = 'linear';
        toastr.options.showMethod = 'fadeIn';
        toastr.options.hideMethod = 'fadeOut';
        toastr.options.toastClass = 'alert';
        toastr.options.titleClass = 'alert-heading';
        toastr.options.progressClass = 'alert-progress';
        toastr.options.closeHtml = `<button type="button" class="close" aria-label="Close"><span aria-hidden="true">&times;</span></button>`;
        toastr.options.messageClass = 'alert-body';
        toastr.options.iconClasses = {
            error: 'alert-danger alert--with-icon',
            info: 'alert-info alert--with-icon',
            success: 'alert-success alert--with-icon',
            warning: 'alert-warning alert--with-icon'
        };
        if(position == 'top-center') {
            toastr.options.target = '#alert-container';
        } else {
            toastr.options.positionClass = `alert-${position}`;
        }
    }

    public static show(message: string, type: AlertType = 'success', position: AlertPosition = 'top-center'): void {
        Alert._setToasterOptions(position);

        let messageHtml: HTMLElement = document.createElement('span');
        messageHtml.innerHTML = message;
        let a = messageHtml.querySelectorAll('a');
        for (let i = 0; i < a.length; i++) {
            a[i].classList.add('alert-link');
        }

        switch (type) {
            case 'error':
                toastr.error(messageHtml.innerHTML);
                break;
            case 'warning':
                toastr.warning(messageHtml.innerHTML);
                break;
            case 'info':
                toastr.info(messageHtml.innerHTML);
                break;
            default:
                toastr.success(messageHtml.innerHTML);
                break;
        }
    }
}