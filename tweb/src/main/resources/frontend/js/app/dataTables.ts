import * as $ from 'jquery';
import 'datatables.net-responsive-bs4';

class DataTable {
    constructor() {
        (<any>$('.dt-responsive')).DataTable();
    }
}

new DataTable();
