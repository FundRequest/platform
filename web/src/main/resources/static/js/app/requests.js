define(function (require) {
    var $ = require('jquery'),
        datatables = require('datatables.net-bs');


    $(function () {
        $('#requests-table').DataTable();
    });
});