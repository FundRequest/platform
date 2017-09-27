import { Component, OnInit } from '@angular/core';
declare var $: any;

@Component({
    selector: 'app-compose',
    templateUrl: './compose.component.html',
    styleUrls: ['./compose.component.scss']
})
export class ComposeComponent implements OnInit {

    showCC = false;
    showBCC = false;
    contents: string;

    constructor() { }

    ngOnInit() {
        // Summernote is currently not ported as a native angular2 module
        // For a quick use it we use the component a wrapper
        // Plugin events can be used to keep component props
        // in sync with the editor content
        $('#summernote').summernote({
            height: 230,
            dialogsInBody: true,
            callbacks: {
                onChange: (contents, $editable) => {
                    this.contents = contents;
                    // console.log(contents);
                }
            }
        });

    }

}
