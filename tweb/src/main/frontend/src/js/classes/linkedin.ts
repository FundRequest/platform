import {Alert} from './alert';
import * as $ from 'jquery';
import {Utils} from './Utils';

interface LinkedInData {
    id: string;
    title: string;
    description: string;
    submittedUrl: string;
    submittedImageUrl: string;
}

class LinkedIn {
    private _postId = null;

    constructor() {
        let button = document.querySelector('[data-share-linked-in="button"]');
        let modal = document.querySelector('#modal-share-linked-in');

        if (button != null) {
            this._addButtonEvents(button, '/bounties/linkedin', {'post-id': this._postId}, modal);
            this._addModalEvents(modal);
        }
    }

    private _addButtonEvents(button, address, data, modal) {
        button.addEventListener('click', (e) => {
            Utils.fetchJSON(address, data)
                .then(() => {
                    $(modal).modal('hide');
                    Alert.show('Sharing is caring, thanks!');
                })
                .catch(() => {
                    Alert.show('Oops, something went wrong, please try again.', 'danger');
                });
        });
    }

    private _addModalEvents(modal) {
        $(modal).on('show.bs.modal', (e) => {
            let target: HTMLElement = (e.target as HTMLElement);
            let title: HTMLElement = target.querySelector('[data-share-linked-in="title"]') as HTMLElement;
            let message: HTMLElement = target.querySelector('[data-share-linked-in="text"]') as HTMLElement;
            let image: HTMLImageElement = target.querySelector('[data-share-linked-in="image"]') as HTMLImageElement;
            let url: HTMLAnchorElement = target.querySelector('[data-share-linked-in="url"]') as HTMLAnchorElement;

            Utils.fetchJSON('/bounties/linkedin/random-post')
                .then((data: LinkedInData) => {
                    this._postId = data.id;
                    url ? url.href = data.submittedUrl : null;
                    image ? image.src = data.submittedImageUrl : null;
                    title ? title.innerHTML = data.title : null;
                    message ? message.innerHTML = data.description : null;
                })
                .catch(function (ex) {
                    console.log('Something when wrong during getting LinkedIn post', ex);
                });
        });
    }
}

new LinkedIn();