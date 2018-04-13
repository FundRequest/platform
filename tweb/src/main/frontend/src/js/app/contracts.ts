import {Web3} from "./web3";
import {FundRequestContract} from "../contracts/FundRequestContract";
import {FundRequestToken} from "../contracts/FundRequestToken";
import {FundRepository} from "../contracts/FundRepository";

export class Contracts {

    private static instance: Contracts;

    public tokenContractAddress : string;
    public frContractAddress    : string;
    private _tokenContract: Promise<FundRequestToken> = null;
    private _frContract: Promise<FundRequestContract> = null;
    private _fundRepository: Promise<FundRepository> = null;
    private _web3: any = Web3.getInstance();

    private constructor() {
        let metaFundRequestToken = document.head.querySelector('[property="contracts:FundRequestToken"]');
        let metaFundRequestContract = document.head.querySelector('[property="contracts:FundRequestContract"]');
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

    public async getFundRepository(): Promise<FundRepository> {
        if (!this._fundRepository) {
            let repositoryAddress: string = await (await this.getFrContract()).fundRepository;
            this._fundRepository = FundRepository.createAndValidate(this._web3, repositoryAddress);
        }
        return this._fundRepository;
    }

    public static getInstance() {
        if (!Contracts.instance) {
            Contracts.instance = new Contracts();
        }
        return Contracts.instance;
    }
}