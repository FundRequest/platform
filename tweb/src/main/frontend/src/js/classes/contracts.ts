import {Web3x} from "./Web3x";
import {FundRequestContract} from "../contracts/FundRequestContract";
import {FundRequestToken} from "../contracts/FundRequestToken";
import {FundRepository} from "../contracts/FundRepository";
import Utils from "./Utils";
import {TokenInfo} from "./token-info";
import {MiniMeToken} from "../contracts/MiniMeToken";

export class Contracts {

    private static instance: Contracts;

    public tokenContractAddress: string;
    public frContractAddress: string;
    private _tokenContract: Promise<FundRequestToken> = null;
    private _frContract: Promise<FundRequestContract> = null;
    private _erc20Contract: Map<string, Promise<MiniMeToken>> = new Map<string, Promise<MiniMeToken>>();
    private _fundRepository: Promise<FundRepository> = null;
    private _web3: any = Web3x.getInstance();

    constructor() {
        let metaFundRequestToken = document.head.querySelector('[name="contracts:FundRequestToken"]');
        let metaFundRequestContract = document.head.querySelector('[name="contracts:FundRequestContract"]');
        this.tokenContractAddress = metaFundRequestToken ? metaFundRequestToken.getAttribute('content') : null;
        this.frContractAddress = metaFundRequestContract ? metaFundRequestContract.getAttribute('content') : null;
    }

    public getTokenContract(): Promise<FundRequestToken> {
        if (!this._tokenContract) {
            this._tokenContract = FundRequestToken.createAndValidate(this._web3, Contracts.getInstance().tokenContractAddress);
        }
        return this._tokenContract;
    }

    public getFrContract(): Promise<FundRequestContract> {
        if (!this._frContract) {
            this._frContract = FundRequestContract.createAndValidate(this._web3, Contracts.getInstance().frContractAddress);
        }
        return this._frContract;
    }

    public getErc20Contract(address: string): Promise<MiniMeToken> {
        if (!this._erc20Contract.get(address)) {
            this._erc20Contract.set(address, MiniMeToken.createAndValidate(this._web3, address));
        }
        return this._erc20Contract.get(address);
    }

    public async getFundRepository(): Promise<FundRepository> {
        if (!this._fundRepository) {
            let repositoryAddress: string = await (await this.getFrContract()).fundRepository;
            this._fundRepository = FundRepository.createAndValidate(this._web3, repositoryAddress);
        }
        return this._fundRepository;
    }

    public static async getErc20Balance(account: string, token: TokenInfo): Promise<Number> {
        if (token) {
            let erc20 = await Contracts.getInstance().getErc20Contract(token.address);
            return (await erc20.balanceOf(account)).toNumber();
        } else {
            return 0;
        }
    }

    public static getPossibleTokens(platformId: string): Promise<TokenInfo[]> {
        return Utils.getJSON("/rest/fund/allowed-tokens?platform=GITHUB&platformId=" + encodeURIComponent(platformId));
    }

    public static getInstance() {
        if (!Contracts.instance) {
            Contracts.instance = new Contracts();
        }
        return Contracts.instance;
    }
}
