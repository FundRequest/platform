import {Utils} from './Utils';

export class DynamicContent {
    private _document: HTMLDocument = document;

    constructor() {
        let $elements = Array.from(this._document.querySelectorAll("[data-dynamic-content]"));
        console.log($elements);
        $elements.forEach((element: HTMLElement) => {
            let path = element.dataset.dynamicContent;
            if (path.length > 0) {
                Utils.fetchHTML(path).then((html) => {
                    element.innerHTML = html;
                    $(element).find(".auto-tooltip").tooltip({});
                });
            }
        })
    }
}
