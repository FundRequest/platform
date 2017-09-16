define(['require', 'jquery', 'components/contract'], function (require, $, contract) {
  $(function () {
    if (contract.userIsUsingDappBrowser()) {
      var $requestBalance = $('#request-balance');
      contract.getRequestBalance($requestBalance.data('request-id'), function (err, result) {
          $requestBalance.html(result);
      });
    }
  });
});