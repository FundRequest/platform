<template>
    <div class="issue-list__item" v-if="req != null">
        <div class="issue-list__item__logo">
            <div><img v-bind:src="`${req.icon}?size=35`" /></div>
            <div>{{req.owner}}</div>
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
        <div class="issue-list__item__fund" v-if="req.otherFunds != null">
            <span class="issue-list__item__fund-amount">{{req.otherFunds.totalAmount}}</span>
            <span class="issue-list__item__fund-currency">{{req.otherFunds.tokenSymbol}}</span>
        </div>
        <div class="issue-list__item__fund" v-if="req.fndFunds != null">
            <span class="issue-list__item__fund-amount">{{req.fndFunds.totalAmount}}</span>
            <span class="issue-list__item__fund-currency">{{req.fndFunds.tokenSymbol}}</span>
        </div>
        <div class="issue-list__item__actions">
            <i class="fal fa-ellipsis-v fa-2x text-warning"></i>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import RequestListItemDto from '../../../app/dto/RequestListItemDto';

    @Component
    export default class RequestListItem extends Vue {
        @Prop({required: true}) request!: any;

        public get req() {
            return Object.assign(new RequestListItemDto(), this.request);
        }
    }
</script>

<style></style>
