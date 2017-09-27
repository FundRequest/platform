import { Component, OnInit } from '@angular/core';

const swal = require('sweetalert');

@Component({
    selector: 'app-sweetalert',
    templateUrl: './sweetalert.component.html',
    styleUrls: ['./sweetalert.component.scss']
})
export class SweetalertComponent implements OnInit {

    constructor() { }

    ngOnInit() { }

    sweetalertDemo1() {
        swal('Here\'s a message');
    }

    sweetalertDemo2() {
        swal('Here\'s a message!', 'It\'s pretty, isn\'t it?');
    }

    sweetalertDemo3() {
        swal('Good job!', 'You clicked the button!', 'success');
    }

    sweetalertDemo4() {
        swal({
            title: 'Are you sure?',
            text: 'Your will not be able to recover this imaginary file!',
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: 'Yes, delete it!',
            closeOnConfirm: false
        }, () => {
            swal('Booyah!');
        });
    }

    sweetalertDemo5() {
        swal({
            title: 'Are you sure?',
            text: 'Your will not be able to recover this imaginary file!',
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#DD6B55',
            confirmButtonText: 'Yes, delete it!',
            cancelButtonText: 'No, cancel plx!',
            closeOnConfirm: false,
            closeOnCancel: false
        }, (isConfirm) => {
            if (isConfirm) {
                swal('Deleted!', 'Your imaginary file has been deleted.', 'success');
            } else {
                swal('Cancelled', 'Your imaginary file is safe :)', 'error');
            }
        });
    }

    sweetalertDemo6() {
        swal({
            title: 'Sweet!',
            text: 'Here\'s a custom image.',
            imageUrl: '/assets/img/user/01.jpg'
        });
    }

}
