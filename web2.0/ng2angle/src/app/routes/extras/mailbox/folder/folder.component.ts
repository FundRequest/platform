import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Http } from '@angular/http';

@Component({
    selector: 'app-folder',
    templateUrl: './folder.component.html',
    styleUrls: ['./folder.component.scss']
})
export class FolderComponent implements OnInit, OnDestroy {

    folder: string = null;
    mails: Array<any> = [];
    private sub: any;

    constructor(public route: ActivatedRoute, public http: Http) {

        this.http.get('assets/server/mails.json')
            .map(data => data.json())
            .subscribe((data) => {
                this.mails = data.mails;
            });

        this.sub = this.route.params.subscribe(params => {
            this.folder = params['folder'] === 'inbox' ? '' : params['folder'];
        });
    }

    ngOnInit() {

    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

    isMailOfFolder(mail) {
        return mail.folder === this.folder;
    }

    folderMailsCount() {
        return this.folderMails().length;
    }

    folderMails() {
        return this.mails.filter(m => (m.folder === this.folder || this.folder === ''))
    }
}
