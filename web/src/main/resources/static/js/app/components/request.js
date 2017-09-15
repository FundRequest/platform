define(['require', 'jquery', 'components/contract'], function (require, $, contract) {
  $(function () {
    if (contract.userIsUsingDappBrowser()) {
      var $issueBalance = $('#issue-balance');
      contract.getIssueBalance($issueBalance.data('request-id'), function (err, result) {
        $issueBalance.html(result + " FND");
      });
    }
  });

});