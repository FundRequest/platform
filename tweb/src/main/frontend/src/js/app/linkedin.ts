import {Alert} from './alert';
import * as $ from 'jquery';

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
            button.addEventListener('click', (e) => {
                $.post('/bounties/linkedin', {'post-id': this._postId}).promise()
                    .then(() => {
                        $(modal).modal('hide');
                        Alert.show('Sharing is caring, thanks!');
                    })
                    .catch(() => {
                        Alert.show('Oops, something went wrong, please try again.', 'danger');
                    })
            });

            $(modal).on('show.bs.modal', (e) => {
                let target: HTMLElement = (e.target as HTMLElement);
                let title: HTMLElement = target.querySelector('[data-share-linked-in="title"]') as HTMLElement;
                let message: HTMLElement = target.querySelector('[data-share-linked-in="text"]') as HTMLElement;
                let image: HTMLImageElement = target.querySelector('[data-share-linked-in="image"]') as HTMLImageElement;
                let url: HTMLAnchorElement = target.querySelector('[data-share-linked-in="url"]') as HTMLAnchorElement;

                this._getLinkedInPost()
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

    private _getLinkedInPost(): Promise<any> {
        return $.get('/bounties/linkedin/random-post').promise();

        /*await fetch('/bounties/linkedin/random-post', {mode: 'no-cors'})
            .then((response) => response.json())
            .catch(function (ex) {
                console.log('Something when wrong during getting LinkedIn post', ex);
            }) as LinkedInData;*/
    }
}

new LinkedIn();