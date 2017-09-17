define(['require', 'jquery', 'components/contract'], function(require, $, contract) {
    var $document = $(document);

    function setBalances($balances) {
        $balances.each(function() {
            var $balance = $(this);
            var requestId = $balance.data('request-balance');
            contract.getRequestBalance(requestId, function(err, result) {
                $balance.html(result);
            });
        });
    }

    $document.on('fnd.requests.balance.update', function(event, data) {
        if (contract.userIsUsingDappBrowser()) {
            var $balances;

            if (typeof data === 'undefined') {
                $balances = $('[data-request-balance]');
            } else {
                if (typeof data.content !== 'undefined') {
                    $balances = $('[data-request-balance]', data.content);
                } else if (typeof data.requestId !== 'undefined') {
                    $balances = $('[data-request-balance="' + data.requestId + '"]');
                }
            }

            setBalances($balances);
        } else {
            $('[data-fund-request]').attr('disabled', 'disabled');
        }
    });

    $(function() {
        $document.trigger('fnd.requests.balance.update');
    });
});
