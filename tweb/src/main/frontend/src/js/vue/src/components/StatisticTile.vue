<template>
    <div class="card bg-primary text-white">
        <div class="card-body media align-items-center">
            <div class="w-25 mr-3 text-center">
                <i class="fa-3x fa" :class="iconClass"></i>
            </div>
            <div class="media-body">
                <h3>{{value}}</h3>
                <div>{{description}}</div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import { Vue, Component, Prop } from "vue-property-decorator";
    //import {Utils} from '../../../app/Utils';
   // import {FundRequestToken} from '../../../contracts/FundRequestToken';
   // import * as Web3 from 'web3';


    @Component
    export default class StatisticTile extends Vue {
        @Prop({required: true}) iconClass!: string;
        @Prop({required: true}) value!: string;
        @Prop({required: true}) description!: string;

      /*  const web3 = new Web3(new Web3.providers.HttpProvider("http://localhost:8545"));

        private async getRequestsFunded() {
            const fundRequestToken = await FundRequestToken.createAndValidate(web3, Utils.getFundRequestTokenAddress())

        }
        private async setContracts() {
            this._tokenContract = this._web3.eth.contract(tokenAbi).at(this.getTokenContractAddress());
            this._fundRequestContract = this._web3.eth.contract(fundRequestAbi).at(this.getFundRequestContractAddress());

            this._fundRepositoryContract = this._web3.eth.contract(fundRepositoryAbi).at(await this.getFundRepositoryContractAddress());
            this._claimRepositoryContract = this._web3.eth.contract(claimRepositoryAbi).at(await this.getClaimRepositoryContractAddress());
        };  public async getRequestsFunded(): Promise<number> {
            return new Promise((resolve, reject) => {
                this._fundRepositoryContract.requestsFunded.call(function (err, result) {
                    err ? reject(err) : resolve(result);
                });
            }) as Promise<number>;
        }*/
    }
</script>

<style></style>
