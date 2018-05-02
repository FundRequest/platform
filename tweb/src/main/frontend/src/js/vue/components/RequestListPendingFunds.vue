<template>
    <div class="request-list__block card" v-if="funds && funds.length > 0">
        <request-list-item-pending-funds v-for="fund in funds" v-bind:request="fund"
                                    v-bind:key="fund.id"></request-list-item-pending-funds>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import RequestListItemPendingFunds from './RequestListItemPendingFunds';
    import {RequestListItemPendingFundDto} from '../dtos/RequestListItemPendingFundDto';

    @Component({
        components: {
            RequestListItemPendingFunds
        }
    })
    export default class RequestListItemPendingFund extends Vue {
        @Prop({required: true}) funds!: RequestListItemPendingFundDto[];
    }
</script>

<style></style>
