import { Component, OnInit, ViewChild } from '@angular/core';
import { ImageCropperComponent, CropperSettings, Bounds } from 'ng2-img-cropper';

@Component({
    selector: 'app-cropper',
    templateUrl: './cropper.component.html',
    styleUrls: ['./cropper.component.scss']
})
export class CropperComponent implements OnInit {

    name: string;
    data1: any;
    cropperSettings: CropperSettings;

    @ViewChild('cropper', undefined) cropper: ImageCropperComponent;

    constructor() {
        this.name = 'Angular2';
        this.cropperSettings = new CropperSettings();

        this.cropperSettings.noFileInput = true;

        this.cropperSettings.width = 200;
        this.cropperSettings.height = 200;

        this.cropperSettings.croppedWidth = 200;
        this.cropperSettings.croppedHeight = 200;

        this.cropperSettings.canvasWidth = 460;
        this.cropperSettings.canvasHeight = 400;

        this.cropperSettings.minWidth = 100;
        this.cropperSettings.minHeight = 100;

        this.cropperSettings.cropperDrawSettings.strokeColor = 'rgba(255,255,255,1)';
        this.cropperSettings.cropperDrawSettings.strokeWidth = 2;

        this.cropperSettings.rounded = false;

        this.data1 = {};
    }

    setRoundedMethod(value: boolean) {
        this.cropperSettings.rounded = value;
    }

    cropped(bounds: Bounds) {
        console.log(bounds);
    }

    fileChangeListener($event) {
        let image: any = new Image();
        let file: File = $event.target.files[0];
        let myReader: FileReader = new FileReader();
        let that = this;
        myReader.onloadend = function(loadEvent: any) {
            image.src = loadEvent.target.result;
            that.cropper.setImage(image);
        };

        myReader.readAsDataURL(file);
    }

    ngOnInit() {
    }

}
