import {FundRequestToken} from '../contracts/FundRequestToken';
import {FundRequestContract} from '../contracts/FundRequestContract';
import {Utils} from './Utils';
import {Web3} from './web3';
import {Contracts} from "./contracts";

class Requests {
    private _account = '0xc31Eb6E317054A79bb5E442D686CB9b225670c1D';
    private _tokenContractZRXAddress = '0x6ff6c0ff1d68b964901f986d4c9fa3ac68346570';
    private _frContractAddress = Contracts.getInstance().frContractAddress;

    private _tokenContract: FundRequestToken = null;
    private _tokenContractZRX: FundRequestToken = null;
    private _frContract: FundRequestContract = null;

    private _web3: any = Web3.getInstance();

    constructor() {
        this._init();
        this._initButtons();
    }

    private async _init() {
        await Promise.all([
            Contracts.getInstance().getTokenContract().then(c => this._tokenContract = c),
            FundRequestToken.createAndValidate(this._web3, this._tokenContractZRXAddress).then(c => this._tokenContractZRX = c),
            Contracts.getInstance().getFrContract().then(c => this._frContract = c)
        ]);
    }

    private _initButtons() {
        document.getElementById('#tempFund').addEventListener('click', () => {
            let issue = (<HTMLInputElement>document.getElementById('#tempFundNumber')).value;
            let amount = parseFloat((<HTMLInputElement>document.getElementById('#tempFundAmount')).value) * Math.pow(10, 18);
            console.log('funding issue: ' + issue + ' with amount ' + amount);
            this._tokenContract.allowance(this._account, this._frContractAddress).then((res) => {
                console.log(res);
            });
            this._tokenContract.approveAndCallTx(this._frContractAddress, amount,
                this._web3.fromAscii('GITHUB' + '|AAC|' + 'FundRequest|FR|area51|FR|' + issue))
                .send({from: this._account, gas: 300000})
                .then((x) => console.log(x));
        });

        document.getElementById('#tempZrxFund').addEventListener('click', () => {
            let issue = (<HTMLInputElement>document.getElementById('#tempFundNumber')).value;
            let amount = parseFloat((<HTMLInputElement>document.getElementById('#tempFundAmount')).value) * Math.pow(10, 18);

            let fundErc20 = () => {
                this._frContract.fundTx(this._web3.fromAscii('GITHUB'), 'FundRequest|FR|area51|FR|' + issue, this._tokenContractZRXAddress, amount)
                    .send({from: this._account, gas: 300000})
                    .then((response) => {
                        console.log('response: ' + response);
                    });
            };

            this._tokenContractZRX.allowance(this._account, this._frContractAddress).then((res) => {
                let allowance = res.toNumber();
                console.log(allowance, amount);

                if (allowance > 0 && allowance < amount) {
                    console.log('setting to 0 first');
                    this._tokenContractZRX.approveTx(this._frContractAddress, 0)
                        .send({from: this._account, gas: 300000})
                        .then((res) => {
                            this._tokenContractZRX.approveTx(this._frContractAddress, amount)
                                .send({from: this._account, gas: 300000})
                                .then(() => fundErc20());
                        });
                } else if (allowance === 0) {
                    this._tokenContractZRX.approveTx(this._frContractAddress, amount)
                        .send({from: this._account, gas: 300000})
                        .then(() => fundErc20());
                } else {
                    fundErc20();
                }
            });
        });
    }
}

Utils.loadOnPageReady(() => {
    new Requests();
});
