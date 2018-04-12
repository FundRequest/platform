import {Web3} from "./web3";
import {FundRequestContract} from "../contracts/FundRequestContract";
import {FundRequestToken} from "../contracts/FundRequestToken";
import {FundRepository} from "../contracts/FundRepository";

export class Contracts {

    private static instance: Contracts;

    public static tokenContractAddress = document.head.querySelector('[property="contracts:FundRequestToken"]').getAttribute('content');
    public static frContractAddress = document.head.querySelector('[property="contracts:FundRequestContract"]').getAttribute('content');
    private _tokenContract: Promise<FundRequestToken> = null;
    private _frContract: Promise<FundRequestContract> = null;
    private _fundRepository: Promise<FundRepository> = null;
    private _web3: any = Web3.getInstance();

    private constructor() {
    }

    public getTokenContract(): Promise<FundRequestToken> {
        if (!this._tokenContract) {
            this._tokenContract = FundRequestToken.createAndValidate(this._web3, Contracts.tokenContractAddress);
        }
        return this._tokenContract;
    }

    public getFrContract(): Promise<FundRequestContract> {
        if (!this._frContract) {
            this._frContract = FundRequestContract.createAndValidate(this._web3, Contracts.frContractAddress);
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

    static getInstance() {
        if (!Contracts.instance) {
            Contracts.instance = new Contracts();
        }
        return Contracts.instance;
    }
}