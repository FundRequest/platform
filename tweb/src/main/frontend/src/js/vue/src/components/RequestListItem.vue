<template>
    <div class="issue-list__item" v-if="req != null" v-on:click="gotoDetails($event.target, req.id)">
        <div class="issue-list__item__logo">
            <div><img v-bind:src="`${req.icon}?size=35`" /></div>
            <div class="issue-list__item__owner" ref="fontSizeFit" style="font-size: 15px">{{req.owner}}</div>
        </div>
        <div class="issue-list__item__info">
            <div class="issue-list__item__header">
                <span class="issue-list__item__title">{{req.title}}</span>
                <span class="issue-list__item__number">#{{req.issueNumber}}</span>
            </div>
            <div class="issue-list__item__status">
                <span class="issue-list__item__badge badge" v-bind:class="`badge--${req.status.toLowerCase()}`">{{req.status}}</span>
                <span class="issue-list__item__tech" v-for="tech in req.technologies">{{tech}}</span>
            </div>
            <div class="issue-list__item__icons">
                <i class="fab fa-github"></i>
                <i class="fab fa-github-alt"></i>
                <i class="fa fa-message"></i>
            </div>
        </div>
        <div class="issue-list__item__price" v-if="req.funds.usdFunds != null">
            <span class="issue-list__item__fund-currency">~$</span>
            <span class="issue-list__item__fund-amount">{{formatPrice(req.funds.usdFunds, 0)}}</span>
        </div>
        <div class="issue-list__item__crypto">
            <div class="issue-list__item__fund" v-if="req.funds.fndFunds != null">
                <span class="issue-list__item__fund-amount">{{formatPrice(req.funds.fndFunds.totalAmount)}}</span>
                <span class="issue-list__item__fund-currency">{{req.funds.fndFunds.tokenSymbol}}</span>
            </div>
            <div class="issue-list__item__fund" v-if="req.funds.otherFunds != null">
                <span class="issue-list__item__fund-amount">{{formatPrice(req.funds.otherFunds.totalAmount)}}</span>
                <span class="issue-list__item__fund-currency">{{req.funds.otherFunds.tokenSymbol}}</span>
            </div>
        </div>

        <div class="issue-list__item__actions" v-on:click="showActions($event.target)">
            <i class="fal fa-ellipsis-v fa-2x text-secondary"></i>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import RequestListItemDto from "../../../app/dto/RequestListItemDto";
    import {Utils} from '../../../app/utils';

    @Component
    export default class RequestListItem extends Vue {
        @Prop({required: true}) request!: any;

        mounted() {
            this._resizeText(this.$refs["fontSizeFit"] as HTMLElement);
        }

        public get req() {
            return Object.assign(new RequestListItemDto(), this.request);
        }

        public formatPrice(value, decimals: number = 2) {
            return Utils.formatTokenPrice(value, decimals);
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
