define(['require', 'jquery', 'components/contract'], function (require, $, contract) {
  $(function () {
    if (contract.userIsUsingDappBrowser()) {
      $('.fnd-balance').each(function () {
        var $this = $(this);
        var issueId = $this.data('issue-id');
        contract.getIssueBalance(issueId, function (err, result) {
          $this.html(result + " FND");
        });
      });

      $('.fund-btn').each(function () {
        var $this = $(this);
        var issueId = $this.data('issue-id');
        var value = 2.6; //todo, user needs to provide this :)
        $this.click(function () {
          contract.fundIssue(issueId, value, function (err, result) {
            if (err) {
              console.error(err);
            } else {
              alert('Issue has been funded');
              console.log(result);
            }
          })
        });
      });
    }
  });

});