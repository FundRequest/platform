import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-forumdiscussion',
    templateUrl: './forumdiscussion.component.html',
    styleUrls: ['./forumdiscussion.component.scss']
})
export class ForumdiscussionComponent implements OnInit {

    answerCollapsed = false;
    topid: number;
    private sub: any;

    constructor(public route: ActivatedRoute) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(params => {
            this.topid = +params['topid']; // (+) converts string param to a number
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

}
