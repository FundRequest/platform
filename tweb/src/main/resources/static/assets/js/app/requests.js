define(["require", "exports", "jquery"], function (require, exports, $) {
  "use strict";
  $(document).ready(function () {
    web3 = new Web3(web3.currentProvider);
    var abi = JSON.parse('[{"constant":true,"inputs":[],"name":"name","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_spender","type":"address"},{"name":"_amount","type":"uint256"}],"name":"approve","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"creationBlock","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"totalSupply","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_from","type":"address"},{"name":"_to","type":"address"},{"name":"_amount","type":"uint256"}],"name":"transferFrom","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"decimals","outputs":[{"name":"","type":"uint8"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_newController","type":"address"}],"name":"changeController","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"_owner","type":"address"},{"name":"_blockNumber","type":"uint256"}],"name":"balanceOfAt","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"version","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_cloneTokenName","type":"string"},{"name":"_cloneDecimalUnits","type":"uint8"},{"name":"_cloneTokenSymbol","type":"string"},{"name":"_snapshotBlock","type":"uint256"},{"name":"_transfersEnabled","type":"bool"}],"name":"createCloneToken","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"_owner","type":"address"}],"name":"balanceOf","outputs":[{"name":"balance","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"isFundRequestToken","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"pure","type":"function"},{"constant":true,"inputs":[],"name":"parentToken","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_owner","type":"address"},{"name":"_amount","type":"uint256"}],"name":"generateTokens","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"symbol","outputs":[{"name":"","type":"string"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"_blockNumber","type":"uint256"}],"name":"totalSupplyAt","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_to","type":"address"},{"name":"_amount","type":"uint256"}],"name":"transfer","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"transfersEnabled","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"parentSnapShotBlock","outputs":[{"name":"","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_spender","type":"address"},{"name":"_amount","type":"uint256"},{"name":"_extraData","type":"bytes"}],"name":"approveAndCall","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_owner","type":"address"},{"name":"_amount","type":"uint256"}],"name":"destroyTokens","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[{"name":"_owner","type":"address"},{"name":"_spender","type":"address"}],"name":"allowance","outputs":[{"name":"remaining","type":"uint256"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_token","type":"address"}],"name":"claimTokens","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"tokenFactory","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_transfersEnabled","type":"bool"}],"name":"enableTransfers","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_spender","type":"address"},{"name":"_currentValue","type":"uint256"},{"name":"_amount","type":"uint256"}],"name":"safeApprove","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"controller","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"inputs":[{"name":"_tokenFactory","type":"address"},{"name":"_parentToken","type":"address"},{"name":"_parentSnapShotBlock","type":"uint256"},{"name":"_tokenName","type":"string"},{"name":"_decimalUnits","type":"uint8"},{"name":"_tokenSymbol","type":"string"},{"name":"_transfersEnabled","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"payable":true,"stateMutability":"payable","type":"fallback"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_token","type":"address"},{"indexed":true,"name":"_controller","type":"address"},{"indexed":false,"name":"_amount","type":"uint256"}],"name":"ClaimedTokens","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_from","type":"address"},{"indexed":true,"name":"_to","type":"address"},{"indexed":false,"name":"_amount","type":"uint256"}],"name":"Transfer","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_cloneToken","type":"address"},{"indexed":false,"name":"_snapshotBlock","type":"uint256"}],"name":"NewCloneToken","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"_owner","type":"address"},{"indexed":true,"name":"_spender","type":"address"},{"indexed":false,"name":"_amount","type":"uint256"}],"name":"Approval","type":"event"}]');
    var contract = web3.eth.contract(abi);


    var frAbi = JSON.parse('[{"constant":true,"inputs":[],"name":"claimSignerAddress","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[{"name":"","type":"uint256"}],"name":"preconditions","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":true,"inputs":[],"name":"fundRepository","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_claimRepository","type":"address"}],"name":"setClaimRepository","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_repositoryAddress","type":"address"}],"name":"setFundRepository","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_index","type":"uint256"}],"name":"removePrecondition","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_precondition","type":"address"}],"name":"addPrecondition","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"owner","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_from","type":"address"},{"name":"_amount","type":"uint256"},{"name":"_token","type":"address"},{"name":"_data","type":"bytes"}],"name":"receiveApproval","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":true,"inputs":[],"name":"claimRepository","outputs":[{"name":"","type":"address"}],"payable":false,"stateMutability":"view","type":"function"},{"constant":false,"inputs":[{"name":"_platform","type":"bytes32"},{"name":"_platformId","type":"string"},{"name":"_token","type":"address"},{"name":"_value","type":"uint256"}],"name":"fund","outputs":[{"name":"success","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_newOwner","type":"address"}],"name":"changeOwner","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"_claimSignerAddress","type":"address"}],"name":"setClaimSignerAddress","outputs":[],"payable":false,"stateMutability":"nonpayable","type":"function"},{"constant":false,"inputs":[{"name":"platform","type":"bytes32"},{"name":"platformId","type":"string"},{"name":"solver","type":"string"},{"name":"solverAddress","type":"address"},{"name":"r","type":"bytes32"},{"name":"s","type":"bytes32"},{"name":"v","type":"uint8"}],"name":"claim","outputs":[{"name":"","type":"bool"}],"payable":false,"stateMutability":"nonpayable","type":"function"},{"inputs":[{"name":"_fundRepository","type":"address"},{"name":"_claimRepository","type":"address"}],"payable":false,"stateMutability":"nonpayable","type":"constructor"},{"payable":false,"stateMutability":"nonpayable","type":"fallback"},{"anonymous":false,"inputs":[{"indexed":true,"name":"from","type":"address"},{"indexed":false,"name":"platform","type":"bytes32"},{"indexed":false,"name":"platformId","type":"string"},{"indexed":false,"name":"token","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Funded","type":"event"},{"anonymous":false,"inputs":[{"indexed":true,"name":"solverAddress","type":"address"},{"indexed":false,"name":"platform","type":"bytes32"},{"indexed":false,"name":"platformId","type":"string"},{"indexed":false,"name":"solver","type":"string"},{"indexed":false,"name":"token","type":"address"},{"indexed":false,"name":"value","type":"uint256"}],"name":"Claimed","type":"event"}]');
    var frContract = web3.eth.contract(frAbi);
    var frContractAddress = '0x0ade7b8f58ba034a2a818f1fd48c3c92039c1cc8';
    var frInstance = frContract.at(frContractAddress);

    $('#tempFund').on('click', function () {
      var instance = contract.at('0x9f88c5cc76148d41a5db8d0a7e581481efc9667b');
      var issue = $('#tempFundNumber').val();
      var amount = $('#tempFundAmount').val() * Math.pow(10, 18);
      console.log('funding issue: ' + issue + ' with amount ' + amount);
      instance.approveAndCall('0x0ade7b8f58ba034a2a818f1fd48c3c92039c1cc8', amount, web3.fromAscii('GITHUB' + "|AAC|" + 'FundRequest|FR|area51|FR|' + issue), {}, function (err, res) {
        console.log(err);
        console.log(res);
      });
    });
    $('#tempZrxFund').on('click', function () {
      console.log('muha');
      var tokenAddress = '0x6ff6c0ff1d68b964901f986d4c9fa3ac68346570';
      var instance = contract.at('0x6ff6c0ff1d68b964901f986d4c9fa3ac68346570');
      var issue = $('#tempFundNumber').val();
      var amount = $('#tempFundAmount').val() * Math.pow(10, 18);

      var logResult = function (err, res) {
        console.log(err);
        console.log(res);
      };

      var fundErc20 = function () {
        frInstance.fund(web3.fromAscii('GITHUB'), 'FundRequest|FR|area51|FR|' + issue, tokenAddress, amount, logResult);
      };


      instance.allowance.call('0xc31Eb6E317054A79bb5E442D686CB9b225670c1D', frContractAddress, function (err, res) {
        var allowance = res.toNumber();
        console.log(allowance);
        console.log(amount);

        if (allowance > 0 && allowance < amount) {
          console.log('setting to 0 first');
          instance.approve(frContractAddress, 0, {}, function (err, res) {
            if (!err) {
              instance.approve(frContractAddress, amount, {}, function (err, res) {
                fundErc20();
              });
            }
          });

        } else if (allowance === 0) {
          instance.approve(frContractAddress, amount, {}, function (err, res) {
            if (!err) {
              fundErc20();
            }
          });
        } else {
          fundErc20();
        }

      });


    });
  });
});
