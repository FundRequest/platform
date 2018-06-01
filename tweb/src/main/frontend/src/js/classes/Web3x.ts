export class Web3x {
    private _web3: any;

    private static instance: Web3x = null;

    private constructor() {
        if (typeof (<any>window).web3 !== 'undefined') {
            this._web3 = new (<any>window).Web3((<any>window).web3.currentProvider);
        } else {
            let endpointUrl = document.head.querySelector("[name=\"ethereum:endpointUrl\"]");
            (<any>window).web3 = new (<any>window).Web3(new (<any>window).Web3.providers.HttpProvider(endpointUrl));
            this._web3 = (<any>window).web3;
            // TODO: make app readonly, no transactions are possible
        }
    }

    public static getInstance(): any {
        if (Web3x.instance == null) {
            Web3x.instance = new Web3x();
        }
        return Web3x.instance._web3;
    }

    public static getAccount(): any {
        return Web3x.getInstance().eth.defaultAccount;
    }
}