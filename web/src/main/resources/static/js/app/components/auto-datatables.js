/**
 * Initializes the jquery/bootstrap datatable plugin for all [data-auto-datatable] items
 */
define(['require', 'jquery'], function(require, $) {
    $(function() {
        var $dataTables = $('[data-auto-datatable]');

        if ($dataTables.length > 0) {
            requirejs(['datatables.net-bs'], function() {
                $dataTables.DataTable();
            });
        }
    });
});