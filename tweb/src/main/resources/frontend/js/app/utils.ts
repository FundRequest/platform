export class Utils {

    public static showLoading() {
        document.querySelector('[data-page-loader]').classList.remove('d-none');
    }

    public static hideLoading() {
        document.querySelector('[data-page-loader]').classList.add('d-none');
    }

    public static getNewWindow(url, widthPopup, heightPopup) {
        let left = (screen.width / 2) - (widthPopup / 2);
        let top = (screen.height / 2) - (heightPopup / 2);
        let newWindow = window.open(null, 'popup', 'toolbar=no, location=no, directories=no, status=no, menubar=no, scrollbars=no, resizable=no, copyhistory=no, width=' + widthPopup + ', height=' + heightPopup + ', top=' + top + ', left=' + left);
        newWindow.opener = null;
        newWindow.location.assign(url);

        return newWindow;
    }

}
