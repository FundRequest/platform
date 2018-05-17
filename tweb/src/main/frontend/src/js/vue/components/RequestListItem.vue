<template>
    <a v-bind:href="getRequestDetailUrl(req.id)" class="request-details request-details--list-item" v-if="req != null">
        <div class="request-details__logo">
            <div><img v-bind:src="`${req.icon}?size=30`" /></div>
            <div class="request-details__owner">
                <font-size-fit v-bind:max-size="15">{{req.owner}}</font-size-fit>
            </div>
        </div>
        <div class="request-details__content">
            <div class="request-details__info">
                <div class="request-details__header">
                    <span class="request-details__title">{{req.title}}</span>
                    <span class="request-details__number">#{{req.issueNumber}}</span>
                </div>
                <div class="request-details__status">
                    <span class="request-details__badge badge badge-pill"
                          v-bind:class="`badge--${req.status.toLowerCase().replace(' ', '_')}`">{{req.status}}</span>
                    <span class="request-details__tech" v-for="tech in req.technologies">{{tech}}</span>
                </div>
                <div class="request-details__icons">
                    <a v-bind:href="getGithubIssueUrl(req.platform, req.owner, req.repo, req.issueNumber)"><i
                            class="fab fa-github"></i></a>
                    <a v-bind:href="`${getRequestDetailUrl(req.id)}#comments`"><i class="fa fa-comment"></i></a>
                    <span>Last modified <timeago :since="req.lastModifiedDate" :auto-update="60" :title="req.lastModifiedDate | toDatetime"></timeago></span>
                </div>
            </div>
            <div class="request-details__funding-details">
                <div class="request-details__price" v-if="req.funds.usdFunds != null">
                    <span class="request-details__fund-currency">$</span>
                    <span class="request-details__fund-amount">{{req.funds.usdFunds, 0 | toUsd}}</span>
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

        <!--div class="request-details__actions" v-on:click.stop="showActions($event)">
            <i class="fal fa-ellipsis-v fa-2x text-secondary"></i>
        </div-->
    </a>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import RequestDto from "../dtos/RequestDto";
    import {Locations} from "../../classes/Locations";
    import VueTimeago from "vue-timeago";
    import FontSizeFit from "./FontSizeFit";
    import ToCrypto from "../filters/formatters/ToCrypto";
    import ToUsd from "../filters/formatters/ToUsd";
    import ToDatetime from '../filters/formatters/ToDatetime';

    Vue.use(VueTimeago, {
        name: "Timeago", // Component name, `Timeago` by default
        locale: undefined // Default locale
    });

    @Component({
        components: {
            FontSizeFit
        },
        filters: {
            toCrypto: ToCrypto.filter,
            toUsd: ToUsd.filter,
            toDatetime: ToDatetime.filter
        }
    })
    export default class RequestListItem extends Vue {
        @Prop({required: true}) request!: any;

        public requestItem: RequestDto = null;

        mounted() {
            this.requestItem = Object.assign(new RequestDto(), this.request);
            this.requestItem.technologies = this.requestItem.technologies.sort();
        }

        public get req() {
            return this.requestItem;
        }

        public showActions(event: Event) {
            //console.log("show actions");
        }

        public getRequestDetailUrl(id) {
            return Locations.getRequestDetailUrl(id);
        }

        public getGithubIssueUrl(platform: string, owner: string, repo: string, issueNumber: string) {
            if (platform.toUpperCase() == "GITHUB") {
                return `https://github.com/${owner}/${repo}/issues/${issueNumber}`;
            }
            return "#no-link";
        }
    }
</script>

<style></style>
