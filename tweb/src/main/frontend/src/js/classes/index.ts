//import 'waves';
//import 'mdb';
import * as $ from 'jquery';

import * as ClipboardJS from 'clipboard';
import Alert from './Alert';
import {InstantEdit} from '../pages/profile/instant-edit';
import {OpenLinkInPopup} from './open-link-in-popup';
import Utils from './Utils';

class Main {
    constructor() {
        this.passGetParams();

        document.addEventListener('browserplugin.from.extension.fnd.opened', (event: CustomEvent) => {
            Utils.openedByBrowserplugin = true;
        });

        $(function () {
            let _clipboard = new ClipboardJS('[data-clipboard-target]');
            _clipboard.on('success', (e) => {
                Alert.success('Copied to your clipboard! ');
                e.clearSelection();
            });
            _clipboard.on('error', () => {
                Alert.warning('This browser doesn\'t allow copying to your clipboard, please do it manually');
            });

            new InstantEdit();
            new OpenLinkInPopup();

            setTimeout(function () {
                document.body.classList.remove('preload');
            }, 500);
        });
    }

    private passGetParams() {
        document.addEventListener('click', function (event: Event) {
            let element = (event.target || event.srcElement) as HTMLElement;
            if (element instanceof HTMLImageElement) {
                element = element.parentElement;
            }

            if (element instanceof HTMLAnchorElement) {
                let anchor = element as HTMLAnchorElement;
                let href = anchor.href;
                if (href) {
                    let hrefBasic = href.match(/^[^\#\?]+/)[0];
                    let locationBasic = location.href.match(/^[^\#\?]+/)[0];

                    if (hrefBasic != locationBasic) {
                        href += (/\?/.test(href) ? '&' : '?') + location.search.substring(1);
                        anchor.href = href;
                    }
                }
            }

        }, false);
    }
}

new Main();