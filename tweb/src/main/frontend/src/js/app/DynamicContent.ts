import * as $ from 'jquery';
import {Utils} from '../classes/Utils';

export class DynamicContent {
    private _document: HTMLDocument = document;

    constructor() {
        let $elements = Array.from(this._document.querySelectorAll("[data-dynamic-content]"));
        $elements.forEach((element: HTMLElement) => {
            let path = element.dataset.dynamicContent;
            if (path.length > 0) {
                Utils.fetchHTML(path).then((html) => {
                    let span = document.createElement('span');
                    span.innerHTML = html;
                    element.parentNode.replaceChild(span, element);
                    this._reinitialize();
                });
            }
        })
    }

    private _reinitialize() {
        let $tooltips = $('[data-toggle="tooltip"]');
        $tooltips.tooltip('dispose');
        $tooltips.tooltip();
    }
}
