import * as $ from 'jquery';

import Utils from '../classes/Utils';

export class DynamicContent {
    private _document: HTMLDocument = document;

    constructor() {
        let $elements = Array.from(this._document.querySelectorAll("[data-dynamic-content]"));
        $elements.forEach((element: HTMLElement) => {
            let path = element.dataset.dynamicContent;
            if (path.length > 0) {
                Utils.getHTML(path).then((html) => {
                    element.innerHTML = html;
                    element.removeAttribute('data-dynamic-content');
                    this._reinitialize(element);
                });
            }
        })
    }

    private _reinitialize(parentElement) {
        $('[data-toggle="tooltip"]', parentElement).tooltip();
    }
}
