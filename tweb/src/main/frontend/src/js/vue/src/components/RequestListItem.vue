<template>
    <div class="request-details request-details--list" v-if="req != null" v-on:click="gotoRequestDetail($event, req.id)">
        <div class="request-details__logo">
            <div><img v-bind:src="`${req.icon}?size=30`" /></div>
            <div class="request-details__owner" ref="fontSizeFit" style="font-size: 15px">{{req.owner}}</div>
        </div>
        <div class="request-details__info">
            <div class="request-details__header">
                <span class="request-details__title">{{req.title}}</span>
                <span class="request-details__number">#{{req.issueNumber}}</span>
            </div>
            <div class="request-details__status">
                <span class="request-details__badge badge" v-bind:class="`badge--${req.status.toLowerCase()}`">{{req.status.toLowerCase()}}</span>
                <span class="request-details__tech" v-for="tech in req.technologies">{{tech}}</span>
            </div>
            <div class="request-details__icons">
                <i class="fab fa-github"></i>
                <i class="fab fa-github-alt"></i>
                <i class="fa fa-message"></i>
            </div>
        </div>
        <div class="request-details__price" v-if="req.funds.usdFunds != null">
            <span class="request-details__fund-currency"><span class="disclaimer-asterix">*</span>$</span>
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

        <div class="request-details__actions" v-on:click.stop="showActions($event)">
            <i class="fal fa-ellipsis-v fa-2x text-secondary"></i>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import RequestDto from "../dtos/RequestDto";
    import {Utils} from '../../../app/Utils';
    import {Locations} from '../../../app/Locations';

    @Component
    export default class RequestListItem extends Vue {
        @Prop({required: true}) request!: any;

        mounted() {
            this._resizeText(this.$refs["fontSizeFit"] as HTMLElement);
        }

        public get req() {
            return Object.assign(new RequestDto(), this.request);
        }

        public formatPrice(value, decimals: number = 2): string {
            return Utils.formatTokenPrice(value, decimals);
        }

        public showActions(event: Event) {
            console.log('show actions');
        }

        public gotoRequestDetail(event, id) {
            Locations.gotoRequestDetail(id);
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
