import {Utils} from "./Utils";

export class OpenLinkInPopup {
    private _document: HTMLDocument = document;

    constructor() {
        let links = [].slice.call(this._document.querySelectorAll('a[data-open-link-in-popup]'));

        links.forEach((item: HTMLAnchorElement) => {
            item.addEventListener('click', (e) => {
                e.preventDefault();
                let newWindow = Utils.getNewWindow(item.href, 600, 600);

                if (window.focus) {
                    newWindow.focus();
                }
                return false;
            });
        });
    }
}
