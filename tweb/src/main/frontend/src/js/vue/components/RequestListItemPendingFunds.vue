<template>
    <div class="request-details request-details--list-item request-details--pending" v-if="req != null"
         v-on:click="gotoDetails($event.target, req.id)">
        <div class="request-details__logo">
            <div><img v-bind:src="`${req.icon}?size=30`" /></div>
            <div class="request-details__owner">
                <font-size-fit v-bind:max-size="15">{{req.issueInformation.owner}}</font-size-fit>
            </div>
        </div>
        <div class="request-details__content">
            <div class="request-details__info">
                <div class="request-details__header">
                    <span class="request-details__title">{{req.issueInformation.title}}</span>
                    <span class="request-details__number">#{{req.issueInformation.number}}</span>
                </div>
                <div class="request-details__status">
                    <span class="request-details__badge badge badge-pill badge--pending">pending</span>
                </div>
                <div class="request-details__icons">
                    <i class="fab fa-github"></i>
                    <i class="fab fa-github-alt"></i>
                    <i class="fa fa-comment"></i>
                </div>
            </div>
            <div class="request-details__funding-details">
                <div class="request-details__price" v-if="req.funds.usdFunds != null">
                    <span class="request-details__fund-currency">$</span>
                    <span class="request-details__fund-amount">{{formatPrice(req.funds.usdFunds, 0)}}</span>
                    <span class="disclaimer-asterix">*</span>
                </div>
                <div class="request-details__crypto">
                    <div class="request-details__fund" v-if="req.funds.fndFunds != null">
                        <span class="request-details__fund-amount">{{formatPrice(req.funds.fndFunds.totalAmount)}}</span>
                        <span class="request-details__fund-currency">{{req.funds.fndFunds.tokenSymbol}}</span>
                    </div>
                    <div class="request-details__fund" v-if="req.funds.otherFunds != null">
                        <span class="request-details__fund-amount">{{formatPrice(req.funds.otherFunds.totalAmount)}}</span>
                        <span class="request-details__fund-currency">{{req.funds.otherFunds.tokenSymbol}}</span>
                    </div>
                </div>
            </div>
        </div>

        <!--div class="request-details__actions" v-on:click="showActions($event.target)">
            <i class="fal fa-ellipsis-v fa-2x text-secondary"></i>
        </div-->
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import {Utils} from "../../classes/Utils";
    import {RequestListItemPendingFundDto} from "../dtos/RequestListItemPendingFundDto";
    import FontSizeFit from "./FontSizeFit";

    @Component
    export default class RequestListItemPendingFund extends Vue {
        @Prop({required: true}) request!: any;

        public get req(): RequestListItemPendingFundDto {
            return Object.assign(new RequestListItemPendingFundDto(), this.request);
        }

        public formatPrice(value, decimals: number = 2) {
            return Utils.formatTokenPrice(value, decimals);
        }

        public showActions(e) {
            null;
        }
    }
</script>

<style></style>
