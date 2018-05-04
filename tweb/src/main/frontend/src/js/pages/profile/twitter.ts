import * as $ from 'jquery';

import {Alert} from '../../classes/alert';
import {Utils} from "../../classes/Utils";

interface VerifyResponse {
    validated: boolean;
    message: string;
}

class Twitter {
    constructor() {
        let modal: HTMLElement = document.querySelector('#modal-twitter-verify') as HTMLElement;
        let button: HTMLElement = document.querySelector('[data-twitter-verify]') as HTMLElement;
        let buttonTweet: HTMLElement = document.querySelector('[data-twitter-tweet]') as HTMLElement;

        if (button != null) {
            button.addEventListener('click', (e) => {
                this._verify(() => {
                    $(modal).modal('hide');
                })
            });

            buttonTweet.addEventListener('click', (e) => {
                let text: string = buttonTweet.dataset.twitterTweet;
                let link: string = `http://twitter.com/home?status=${encodeURIComponent(text)}`;

                let twitterWindow: Window = Utils.getNewWindow(link, 600, 600);
            });
        }
    }

    private _verify(callback = null) {
        Utils.showLoading();
        this._getVerify()
            .then((data: VerifyResponse) => {
                Alert.show(data.message, data.validated ? 'success' : 'danger');
                if (data.validated) {
                    callback != null ? callback() : null;
                }
                Utils.hideLoading();
            })
            .catch(function (ex) {
                Alert.show('Oops, something went wrong, please try again.', 'danger');
                Utils.hideLoading();
            });
    }

    private _getVerify(): Promise<any> {
        return Utils.getJSON('/bounties/twitter/verify');
    }
}


new Twitter();
