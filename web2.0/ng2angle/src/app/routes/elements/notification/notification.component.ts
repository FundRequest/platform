import { Component, OnInit } from '@angular/core';
import { ToasterService, ToasterConfig } from 'angular2-toaster/angular2-toaster';

@Component({
    selector: 'app-notification',
    templateUrl: './notification.component.html',
    styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {
    // TOASTER
    toaster: any;
    toasterConfig: any;
    toasterconfig: ToasterConfig = new ToasterConfig({
        positionClass: 'toast-bottom-right',
        showCloseButton: true
    });

    // ALERTs
    public alerts: Array<Object> = [
        {
            type: 'danger',
            msg: 'Oh snap! Change a few things up and try submitting again.'
        },
        {
            type: 'success',
            msg: 'Well done! You successfully read this important alert message.',
            closable: true
        }
    ];

    // PROGRESSBAR
    public max: number = 200;
    public showWarning: boolean;
    public dynamic: number;
    public type: string;
    public stacked: any[] = [];

    // TOOLTIPS
    public dynamicTooltip: string = 'Hello, World!';
    public dynamicTooltipText: string = 'dynamic';
    public htmlTooltip: string = 'I\'ve been made <b>bold</b>!';
    public tooltipModel: any = { text: 'foo', index: 1 };

    // RATINGS
    public x: number = 5;
    public y: number = 2;
    public maxRat: number = 10;
    public rate: number = 7;
    public isReadonly: boolean = false;
    public overStar: number;
    public percent: number;
    public ratingStates: any = [
        { stateOn: 'fa fa-check', stateOff: 'fa fa-check-circle' },
        { stateOn: 'fa fa-star', stateOff: 'fa fa-star-o' },
        { stateOn: 'fa fa-heart', stateOff: 'fa fa-ban' },
        { stateOn: 'fa fa-heart' },
        { stateOff: 'fa fa-power-off' }
    ];

    constructor(public toasterService: ToasterService) {

        this.toaster = {
            type: 'success',
            title: 'Title',
            text: 'Message'
        };

        this.random();
        this.randomStacked();

    }

    ngOnInit() { }

    // TOSATER METHOD
    pop() {
        this.toasterService.pop(this.toaster.type, this.toaster.title, this.toaster.text);
    };

    // ALERT METHOD
    public closeAlert(i: number): void {
        this.alerts.splice(i, 1);
    }

    public addAlert(): void {
        this.alerts.push({ msg: 'Another alert!', type: 'warning', closable: true });
    }

    // PROGRESSBAR METHODS
    public random(): void {
        let value = Math.floor((Math.random() * 100) + 1);
        let type: string;

        if (value < 25) {
            type = 'success';
        } else if (value < 50) {
            type = 'info';
        } else if (value < 75) {
            type = 'warning';
        } else {
            type = 'danger';
        }

        this.showWarning = (type === 'danger' || type === 'warning');
        this.dynamic = value;
        this.type = type;
    };

    public randomStacked(): void {
        let types = ['success', 'info', 'warning', 'danger'];

        this.stacked = [];
        let total = 0;
        let n = Math.floor((Math.random() * 4) + 1);
        for (let i = 0; i < n; i++) {
            let index = Math.floor((Math.random() * 4));
            let value = Math.floor((Math.random() * 30) + 1);
            total += value;
            this.stacked.push({
                value: value,
                max: value, // i !== (n - 1) ? value : 100,
                type: types[index]
            });
        }
    };

    // TOOLTIPS METHODS
    public tooltipStateChanged(state: boolean): void {
        console.log(`Tooltip is open: ${state}`);
    }

    // RATINGS METHODS
    public hoveringOver(value: number): void {
        this.overStar = value;
        this.percent = 100 * (value / this.maxRat);
    };

    public resetStar(): void {
        this.overStar = void 0;
    }
}
