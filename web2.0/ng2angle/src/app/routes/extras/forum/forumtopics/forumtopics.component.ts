import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-forumtopics',
    templateUrl: './forumtopics.component.html',
    styleUrls: ['./forumtopics.component.scss']
})
export class ForumtopicsComponent implements OnInit, OnDestroy {

    catid: number;
    private sub: any;

    constructor(public route: ActivatedRoute) { }

    ngOnInit() {
        this.sub = this.route.params.subscribe(params => {
            this.catid = +params['catid']; // (+) converts string param to a number
        });
    }

    ngOnDestroy() {
        this.sub.unsubscribe();
    }

}
