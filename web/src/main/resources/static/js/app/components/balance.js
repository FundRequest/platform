define(['require', 'jquery', 'components/contract'], function(require, $, contract) {
    var $document = $(document);

    $document.on('fnd.user.balance.update', function() {
        if (contract.userIsUsingDappBrowser()) {
            contract.getUserBalance(function(err, result) {
                if (err) {
                    alert(err);
                } else {
                    $('[data-user-balance]').html(result);
                }
            });

        }
    });

    $(function() {
        $document.trigger('fnd.user.balance.update');
    });

});