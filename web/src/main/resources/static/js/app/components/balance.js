define(['require', 'jquery', 'components/contract'], function (require, $, contract) {
  $(function () {
    if (contract.userIsUsingDappBrowser()) {
      contract.getUserBalance(function (err, result) {
        if (err) {
          alert(err);
        } else {
          $('#ether-balance').html('Balance: ' + result + " FND");
        }
      });

    }
  });

});