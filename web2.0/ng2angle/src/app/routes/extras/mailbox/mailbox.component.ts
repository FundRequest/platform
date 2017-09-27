import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";

@Component({
    selector: 'app-mailbox',
    templateUrl: './mailbox.component.html',
    styleUrls: ['./mailbox.component.scss']
})
export class MailboxComponent implements OnInit {

    mailboxMenuCollapsed = true;

    folders = [
        { name: 'Inbox', folder: 'inbox', alert: 42, icon: 'fa-inbox' },
        { name: 'Starred', folder: 'starred', alert: 10, icon: 'fa-star' },
        { name: 'Sent', folder: 'sent', alert: 0, icon: 'fa-paper-plane-o' },
        { name: 'Draft', folder: 'draft', alert: 5, icon: 'fa-edit' },
        { name: 'Trash', folder: 'trash', alert: 0, icon: 'fa-trash' }
    ];

    labels = [
        { name: 'Red', color: 'danger' },
        { name: 'Pink', color: 'pink' },
        { name: 'Blue', color: 'info' },
        { name: 'Yellow', color: 'warning' }
    ];

    mail = {
        cc: false,
        bcc: false
    };
    // Mailbox editr initial content
    content = '<p>Type something..</p>';

    constructor(public router: Router) { }

    routeIsActive(routePath: string) {
        return this.router.url == routePath;
    }

    ngOnInit() {
    }

}
