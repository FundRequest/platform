<template>
    <div class="request-details request-details--list" v-if="req != null" v-on:click="gotoDetails($event.target, req.id)">
        <div class="request-details__logo">
            <div><img v-bind:src="`${req.icon}?size=30`" /></div>
            <div class="request-details__owner" ref="fontSizeFit" style="font-size: 15px">{{req.issueInformation.owner}}</div>
        </div>
        <div class="request-details__info">
            <div class="request-details__header">
                <span class="request-details__title">{{req.issueInformation.title}}</span>
                <span class="request-details__number">#{{req.issueInformation.number}}</span>
            </div>
            <div class="request-details__status">
                <span class="request-details__badge badge badge--pending">pending</span>
            </div>
            <div class="request-details__icons">
                <i class="fab fa-github"></i>
                <i class="fab fa-github-alt"></i>
                <i class="fa fa-message"></i>
            </div>
        </div>
        <div class="request-details__price" v-if="req.funds.usdFunds != null">
            <span class="request-details__fund-currency">~$</span>
            <span class="request-details__fund-amount">{{formatPrice(req.funds.usdFunds, 0)}}</span>
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

        <div class="request-details__actions" v-on:click="showActions($event.target)">
            <i class="fal fa-ellipsis-v fa-2x text-secondary"></i>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import {Utils} from '../../../app/Utils';
    import {RequestListItemPendingFundDto} from '../dtos/RequestListItemPendingFundDto';

    @Component
    export default class RequestListItemPendingFund extends Vue {
        @Prop({required: true}) request!: any;

        mounted() {
            this._resizeText(this.$refs["fontSizeFit"] as HTMLElement);
        }

        public get req(): RequestListItemPendingFundDto {
            return Object.assign(new RequestListItemPendingFundDto(), this.request);
        }

        public formatPrice(value, decimals: number = 2) {
            return Utils.formatTokenPrice(value, decimals);
        }

        public showActions(e) {
            null;
        }

        private _resizeText(el: HTMLElement) {
            if (el) {
                el.style.fontSize = (parseInt(el.style.fontSize.slice(0, -2)) - 1) + "px";
                if (el.offsetWidth > el.parentElement.offsetWidth) {
                    this._resizeText(el);
                }
            }
        }

    }
</script>

<style></style>
