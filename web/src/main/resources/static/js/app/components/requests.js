define(['require', 'jquery', 'components/contract'], function(require, $, contract) {
    $(function() {
        if (contract.userIsUsingDappBrowser()) {
            $('[data-request-balance]').each(function() {
                var $balance = $(this);
                var requestId = $balance.data('request-balance');
                contract.getRequestBalance(requestId, function(err, result) {
                    $balance.html(result);
                });
            });
        } else {
            $('[data-fund-request]').disable();
        }
    });
});