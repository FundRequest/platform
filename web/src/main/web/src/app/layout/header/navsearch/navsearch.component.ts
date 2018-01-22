import { Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChange } from '@angular/core';

declare var $: any;

@Component({
  selector   : 'app-navsearch',
  templateUrl: './navsearch.component.html',
  styleUrls  : ['./navsearch.component.scss']
})
export class NavsearchComponent implements OnInit, OnChanges {

  @Input() visible: boolean;
  @Output() onclose = new EventEmitter<boolean>();
  term: string;

  constructor(public elem: ElementRef) { }

  ngOnInit() {
    $(document)
      .on('keyup', event => {
        if (event.keyCode === 27) {// ESC
          this.closeNavSearch();
        }
      })
      .on('click', event => {
        if (!$.contains(this.elem.nativeElement, event.target)) {
          this.closeNavSearch();
        }
      })
    ;
  }

  closeNavSearch() {
    this.visible = false;
    this.onclose.emit();
  }

  ngOnChanges(changes: { [propKey: string]: SimpleChange }) {
    if (changes['visible'].currentValue === true) {
      this.elem.nativeElement.querySelector('input').focus();
    }
  }

  handleForm() {
    console.log('Form submit: ' + this.term);
  }
}
