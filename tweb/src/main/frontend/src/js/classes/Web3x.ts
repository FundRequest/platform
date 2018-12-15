export class Web3x {
    private _web3: any;

    private static instance: Web3x = null;

    private constructor(provider: any) {
        this._web3 = new (<any>window).Web3(provider);
        if (typeof (<any>window).web3 === 'undefined') {
            (<any>window).web3 = this._web3;
        }
    }

    public static getInstance(): any {
        if (Web3x.instance == null) {
            let provider;
            const endpointUrl:any = document.head.querySelector("[name=\"ethereum:endpointUrl\"]");
            provider = new (<any>window).Web3.providers.HttpProvider(endpointUrl.content);
            Web3x.instance = new Web3x(provider);
        }
        return Web3x.instance._web3;
    }

}