define(function (require) {
    var $ = require('jquery');
    require('datatables.net-bs');

    $(function () {
        $('.auto-datatable').DataTable();
    });

});