import Web3Dist from 'web3';

export class Web3 {
    private _web3: any;

    private static instance: Web3;

    private constructor() {
        if (typeof (<any>window).web3 !== 'undefined') {
            this._web3 = new (<any>window).Web3((<any>window).web3.currentProvider);
        } else {
            (<any>window).web3 = new Web3Dist();
            this._web3 = new (<any>window).Web3(new (<any>window).web3.providers.HttpProvider('https://kovan.fundrequest.io'));
            // TODO: make app readonly, no transactions are possible
        }
    }

    static getInstance():any {
        if (!Web3.instance) {
            Web3.instance = new Web3();
        }
        return Web3.instance._web3;
    }
}