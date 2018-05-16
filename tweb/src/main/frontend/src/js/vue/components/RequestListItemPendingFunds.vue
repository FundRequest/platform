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
                    <a v-bind:href="getGithubIssueUrl(req.platform, req.owner, req.repo, req.number)"><i class="fab fa-github"></i></a>
                </div>
            </div>
            <div class="request-details__funding-details">
                <div class="request-details__price" v-if="req.funds.usdFunds != null">
                    <span class="request-details__fund-currency">$</span>
                    <span class="request-details__fund-amount">{{req.funds.usdFunds | toUsd}}</span>
                    <span class="disclaimer-asterix">*</span>
                </div>
                <div class="request-details__crypto">
                    <div class="request-details__fund" v-if="req.funds.fndFunds != null">
                        <span class="request-details__fund-amount">{{req.funds.fndFunds.totalAmount | toCrypto}}</span>
                        <span class="request-details__fund-currency">{{req.funds.fndFunds.tokenSymbol}}</span>
                    </div>
                    <div class="request-details__fund" v-if="req.funds.otherFunds != null">
                        <span class="request-details__fund-amount">{{req.funds.otherFunds.totalAmount | toCrypto}}</span>
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
    import ToCrypto from '../filters/formatters/ToCrypto';
    import {RequestListItemPendingFundDto} from "../dtos/RequestListItemPendingFundDto";
    import FontSizeFit from "./FontSizeFit";

    @Component({
        components: {
            FontSizeFit
        },
        filters: {
            toCrypto: ToCrypto.filter
        }
    })
    export default class RequestListItemPendingFund extends Vue {
        @Prop({required: true}) request!: any;

        public get req(): RequestListItemPendingFundDto {
            return Object.assign(new RequestListItemPendingFundDto(), this.request);
        }

        public showActions(e) {
            null;
        }

        public getGithubIssueUrl(platform: string, owner: string, repo: string, issueNumber: string) {
            if (platform.toUpperCase() == 'GITHUB') {
                return `https://github.com/${owner}/${repo}/issues/${issueNumber}`;
            }
            return "#";
        }
    }
</script>

<style></style>
