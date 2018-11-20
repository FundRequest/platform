export class Web3x {
    private _web3: any;

    private static instance: Web3x = null;

    private constructor(provider: any) {
        this._web3 = new (<any>window).Web3(provider);
        if (typeof (<any>window).web3 === 'undefined') {
            (<any>window).web3 = this._web3;
        }
    }

    public  static async getInstance(): Promise<any> {
        if (Web3x.instance == null) {
            let provider;
            if (typeof (<any>window).web3 !== 'undefined') {
                provider = (<any>window).web3.currentProvider;
            } else {
                const endpointUrl = document.head.querySelector("[name=\"ethereum:endpointUrl\"]");
                provider = new (<any>window).Web3.providers.HttpProvider(endpointUrl);
                // TODO: make app readonly, no transactions are possible
            }

            Web3x.instance = new Web3x(provider);
            if (Web3x.instance._web3.currentProvider) {
                await Web3x.instance._web3.currentProvider.enable();
            }
        }
        return Web3x.instance._web3;
    }

    public static async getAccount(): Promise<any> {
        return (await Web3x.getInstance()).eth.defaultAccount;
    }
}