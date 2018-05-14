//import 'waves';
//import 'mdb';
import * as $ from 'jquery';

import * as ClipboardJS from 'clipboard';
import Alert from './Alert';
import {InstantEdit} from '../pages/profile/instant-edit';
import {OpenLinkInPopup} from './open-link-in-popup';

class Main {
    constructor() {
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
}

new Main();