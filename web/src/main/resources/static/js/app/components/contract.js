define(['require', 'jquery'], function (require, $) {
  var $document = $(document);

  var
    tokenContract,
    fundRequestContract;

  if (typeof web3 !== 'undefined') {
    window.web3 = new Web3(web3.currentProvider);
    if (web3.version.network !== '4') {
      alert('Please connect to the Rinkeby network');
      //TODO redirect to a page with some extra explanation
    } else {
      initTokenContract();
      initContract();
    }
  } else {
    alert('Please use a dapp browser like mist or MetaMask plugin for chrome');
  }

  function initTokenContract() {
    var tokenAbi = [{
      "constant": false,
      "inputs": [{"name": "addr", "type": "address"}, {"name": "state", "type": "bool"}],
      "name": "setTransferAgent",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "mintingFinished",
      "outputs": [{"name": "", "type": "bool"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "name",
      "outputs": [{"name": "", "type": "string"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "_spender", "type": "address"}, {"name": "_value", "type": "uint256"}],
      "name": "approve",
      "outputs": [{"name": "success", "type": "bool"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "_fundRequestContractAddress", "type": "address"}],
      "name": "setFundRequestContractAddress",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "totalSupply",
      "outputs": [{"name": "", "type": "uint256"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "_from", "type": "address"}, {"name": "_to", "type": "address"}, {
        "name": "_value",
        "type": "uint256"
      }],
      "name": "transferFrom",
      "outputs": [{"name": "success", "type": "bool"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "addr", "type": "address"}],
      "name": "setReleaseAgent",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "decimals",
      "outputs": [{"name": "", "type": "uint256"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "value", "type": "uint256"}, {"name": "data", "type": "string"}],
      "name": "transferFunding",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "receiver", "type": "address"}, {"name": "amount", "type": "uint256"}],
      "name": "mint",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [{"name": "", "type": "address"}],
      "name": "mintAgents",
      "outputs": [{"name": "", "type": "bool"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "addr", "type": "address"}, {"name": "state", "type": "bool"}],
      "name": "setMintAgent",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "value", "type": "uint256"}],
      "name": "upgrade",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "_name", "type": "string"}, {"name": "_symbol", "type": "string"}],
      "name": "setTokenInformation",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "upgradeAgent",
      "outputs": [{"name": "", "type": "address"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [],
      "name": "releaseTokenTransfer",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "upgradeMaster",
      "outputs": [{"name": "", "type": "address"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [{"name": "_owner", "type": "address"}],
      "name": "balanceOf",
      "outputs": [{"name": "balance", "type": "uint256"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "isFundRequestToken",
      "outputs": [{"name": "", "type": "bool"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "getUpgradeState",
      "outputs": [{"name": "", "type": "uint8"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [{"name": "", "type": "address"}],
      "name": "transferAgents",
      "outputs": [{"name": "", "type": "bool"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "owner",
      "outputs": [{"name": "", "type": "address"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "symbol",
      "outputs": [{"name": "", "type": "string"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "released",
      "outputs": [{"name": "", "type": "bool"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "canUpgrade",
      "outputs": [{"name": "", "type": "bool"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "fundRequestContract",
      "outputs": [{"name": "", "type": "address"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "_to", "type": "address"}, {"name": "_value", "type": "uint256"}],
      "name": "transfer",
      "outputs": [{"name": "success", "type": "bool"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "totalUpgraded",
      "outputs": [{"name": "", "type": "uint256"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "releaseAgent",
      "outputs": [{"name": "", "type": "address"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "agent", "type": "address"}],
      "name": "setUpgradeAgent",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [{"name": "_owner", "type": "address"}, {"name": "_spender", "type": "address"}],
      "name": "allowance",
      "outputs": [{"name": "remaining", "type": "uint256"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "isToken",
      "outputs": [{"name": "weAre", "type": "bool"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "newOwner", "type": "address"}],
      "name": "transferOwnership",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "master", "type": "address"}],
      "name": "setUpgradeMaster",
      "outputs": [],
      "payable": false,
      "type": "function"
    }, {
      "inputs": [{"name": "_name", "type": "string"}, {
        "name": "_symbol",
        "type": "string"
      }, {"name": "_initialSupply", "type": "uint256"}, {"name": "_decimals", "type": "uint256"}, {
        "name": "_mintable",
        "type": "bool"
      }], "payable": false, "type": "constructor"
    }, {
      "anonymous": false,
      "inputs": [{"indexed": false, "name": "newName", "type": "string"}, {
        "indexed": false,
        "name": "newSymbol",
        "type": "string"
      }],
      "name": "UpdatedTokenInformation",
      "type": "event"
    }, {
      "anonymous": false,
      "inputs": [{"indexed": true, "name": "_from", "type": "address"}, {
        "indexed": true,
        "name": "_to",
        "type": "address"
      }, {"indexed": false, "name": "_value", "type": "uint256"}],
      "name": "Upgrade",
      "type": "event"
    }, {
      "anonymous": false,
      "inputs": [{"indexed": false, "name": "agent", "type": "address"}],
      "name": "UpgradeAgentSet",
      "type": "event"
    }, {
      "anonymous": false,
      "inputs": [{"indexed": false, "name": "addr", "type": "address"}, {
        "indexed": false,
        "name": "state",
        "type": "bool"
      }],
      "name": "MintingAgentChanged",
      "type": "event"
    }, {
      "anonymous": false,
      "inputs": [{"indexed": false, "name": "receiver", "type": "address"}, {
        "indexed": false,
        "name": "amount",
        "type": "uint256"
      }],
      "name": "Minted",
      "type": "event"
    }, {
      "anonymous": false,
      "inputs": [{"indexed": true, "name": "owner", "type": "address"}, {
        "indexed": true,
        "name": "spender",
        "type": "address"
      }, {"indexed": false, "name": "value", "type": "uint256"}],
      "name": "Approval",
      "type": "event"
    }, {
      "anonymous": false,
      "inputs": [{"indexed": true, "name": "from", "type": "address"}, {
        "indexed": true,
        "name": "to",
        "type": "address"
      }, {"indexed": false, "name": "value", "type": "uint256"}],
      "name": "Transfer",
      "type": "event"
    }];
    var tokenContractAddress = "0xb6a0d43b4dd2024861578ae165ced97ec2d70a16";
    tokenContract = web3.eth.contract(tokenAbi).at(tokenContractAddress);
  }

  function initContract() {
    var contractAbi = [{
      "constant": true,
      "inputs": [{"name": "_data", "type": "string"}],
      "name": "balance",
      "outputs": [{"name": "", "type": "uint256"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": false,
      "inputs": [{"name": "_from", "type": "address"}, {"name": "_value", "type": "uint256"}, {
        "name": "_data",
        "type": "string"
      }],
      "name": "fund",
      "outputs": [{"name": "success", "type": "bool"}],
      "payable": false,
      "type": "function"
    }, {
      "constant": true,
      "inputs": [],
      "name": "token",
      "outputs": [{"name": "", "type": "address"}],
      "payable": false,
      "type": "function"
    }, {"inputs": [{"name": "_tokenAddress", "type": "address"}], "payable": false, "type": "constructor"}];
    var contractAddress = "0x71acb72cae766b300d92f01458d46a1c84255bc1";
    fundRequestContract = web3.eth.contract(contractAbi).at(contractAddress);
  }

  function getUserBalance(callback) {
    if (web3.eth.accounts && web3.eth.accounts.length > 0) {
      tokenContract.balanceOf.call(web3.eth.accounts[0], function (err, result) {
        var balance;
        if (result) {
          balance = fromWeiRounded(result);
        }
        callback(err, balance);
      });
    } else {
      callback("No accounts available");
    }
  }

  function fromWeiRounded(amountInWei) {
    var number = (amountInWei.toNumber() / 1000000000000000000);
    return ((Math.round(number * 100) / 100).toFixed(2)).toLocaleString();
  }

  function fundRequest(requestId, value, callback) {
    var total = web3.toWei(value, 'ether');
    tokenContract.transferFunding(total, '' + requestId, function (err, result) {
      callback(err, result);
    });
    $.post("/requests/" + requestId + "/funds", {requestId: requestId, amountInWei: total});
  }

  function userIsUsingDappBrowser() {
    return typeof web3 !== 'undefined'
      && web3.eth.accounts
      && web3.eth.accounts.length > 0;
  }

  function getRequestBalance(requestId, callback) {
    fundRequestContract.balance.call('' + requestId, function (err, result) {
      var balance;
      if (result) {
        balance = fromWeiRounded(result);
      }
      callback(err, balance);
    });
  }

  return {
    getUserBalance: getUserBalance,
    userIsUsingDappBrowser: userIsUsingDappBrowser,
    fundRequest: fundRequest,
    getRequestBalance: getRequestBalance
  }

});