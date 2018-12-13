import {Web3x} from './Web3x';
import {FundRequestContract} from '../contracts/FundRequestContract';
import {FundRequestToken} from '../contracts/FundRequestToken';
import {FundRepository} from '../contracts/FundRepository';
import Utils from './Utils';
import {TokenInfo} from './token-info';
import {MiniMeToken} from '../contracts/MiniMeToken';
import BigNumber from "bignumber.js";
import {ERC20} from "../contracts/ERC20";

export class Contracts {

    private static instance: Contracts;

    private static _tokenContractAddress: string = '';
    private static _frContractAddress: string = '';

    private _tokenContract: Promise<FundRequestToken> = null;
    private _frContract: Promise<FundRequestContract> = null;
    private _erc20Contract: Map<string, Promise<MiniMeToken>> = new Map<string, Promise<MiniMeToken>>();
    private _fundRepository: Promise<FundRepository> = null;
    private _web3: any = null;

    private constructor() {
    }

    private async initialize() {
        this._web3 = await Web3x.getInstance();
    }

    public static get frContractAddress() {
        if (Contracts._frContractAddress.length <= 0) {
            let metaFundRequestContract = document.head.querySelector('[name="contracts:FundRequestContract"]');
            Contracts._frContractAddress = metaFundRequestContract ? metaFundRequestContract.getAttribute('content') : null;
        }
        return Contracts._frContractAddress;
    }

    public static get tokenContractAddress() {
        if (Contracts._tokenContractAddress.length <= 0) {
            let metaFundRequestToken = document.head.querySelector('[name="contracts:FundRequestToken"]');
            Contracts._tokenContractAddress = metaFundRequestToken ? metaFundRequestToken.getAttribute('content') : null;
        }
        return Contracts._tokenContractAddress;
    }

    public static async getTokenContract(): Promise<FundRequestToken> {
        const instance = await Contracts.getInstance();
        if (!instance._tokenContract) {
            instance._tokenContract = FundRequestToken.createAndValidate(instance._web3, Contracts.tokenContractAddress);
        }
        return instance._tokenContract;
    }

    public static async getFrContract(): Promise<FundRequestContract> {
        const instance = await Contracts.getInstance();
        if (!instance._frContract) {
            instance._frContract = FundRequestContract.createAndValidate(instance._web3, Contracts.frContractAddress);
        }
        return instance._frContract;
    }

    public static async getErc20Contract(address: string): Promise<MiniMeToken> {
        const instance = await Contracts.getInstance();
        if (!instance._erc20Contract.get(address)) {
            instance._erc20Contract.set(address, MiniMeToken.createAndValidate(instance._web3, address));
        }
        return instance._erc20Contract.get(address);
    }

    public static async getFundRepository(): Promise<FundRepository> {
        const instance = await Contracts.getInstance();
        if (!instance._fundRepository) {
            let repositoryAddress: string = await (await Contracts.getFrContract()).fundRepository;
            instance._fundRepository = FundRepository.createAndValidate(instance._web3, repositoryAddress);
        }
        return instance._fundRepository;
    }

    public static async getErc20Balance(account: string, token: TokenInfo): Promise<Number> {
        if (token) {
            let erc20 = await await Contracts.getErc20Contract(token.address);
            return (await erc20.balanceOf(account)).toNumber();
        } else {
            return 0;
        }
    }

    public static getPossibleTokens(platformId: string): Promise<TokenInfo[]> {
        return Utils.getJSON('/rest/fund/allowed-tokens?platform=GITHUB&platformId=' + encodeURIComponent(platformId));
    }

    private static async getInstance() {
        if (!Contracts.instance) {
            Contracts.instance = new Contracts();
            await Contracts.instance.initialize();
        }
        return Contracts.instance;
    }


    public static encodeErc20ApproveFunction(address: string, amount: BigNumber) {
        return Web3x.getInstance().eth.contract(ERC20.abi).at("").approve.getData(
            address, amount
        );
    }

    public static encodeFundFunction(platform: string, platformId: string, token: string, amount: BigNumber) {
        let web3 = Web3x.getInstance();
        return web3.eth.contract(FundRequestContract.abi).at("").fund.getData(
            web3.fromAscii("GITHUB"),
            platformId,
            token,
            amount
        );
    }
}
