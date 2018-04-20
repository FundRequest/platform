<template>
    <section class="issue-lists">
        <list-filter
                v-bind:active="statusFilter"
                v-bind:filters="[
                    { value: 'all', title: 'All', description: 'Show All' },
                    { value: 'funded', title: 'Funded' description: 'Show Funded' },
                    { value: 'starred', title: 'Starred', description: 'Show Starred' },
                    { value: 'claimed', title: 'Claimed', description: 'Show Claimed' },
                    { value: 'failed', title: 'Failed', description: 'Show Failed' }
                 ]"
                v-on:update="setStatusFilter"
        />

        <div class="issue-list__block card" v-if="statusFilter==='funded' || statusFilter==='all'">
            <RequestListItemPendingFund v-for="request in pendingRequests" v-bind:request="request"
                                        v-bind:key="request.id"></RequestListItemPendingFund>
        </div>

        <div class="issue-list__options" v-if="!isEmpty">
            <div class="row">
                <div class="col-12 col-md-4 col-lg-3">
                    <div class="md-form">
                        <input class="form-control form-control--search" id="list-search" type="search"
                               v-bind:value="searchFilter"
                               v-on:input="setSearchFilter($event.target.value)" />
                        <label for="list-search"><span>Search</span><i class="fa fa-search"></i></label>
                    </div>
                </div>
                <div class="col-12 col-md-4 offset-md-4 col-lg-3 offset-lg-6">
                    <div class="md-form">
                        <fnd-select v-bind:id="'list-sort'"
                                    v-bind:value="sortBy"
                                    v-on:input="setSortBy">
                            <option value="" selected="selected" disabled="disabled">Sort by</option>
                            <option value="title" selected="selected">Title</option>
                            <option value="fundings">Fundings</option>
                        </fnd-select>
                    </div>
                </div>
            </div>
        </div>
        <div class="issue-list__block card" v-if="!isEmpty">
            <RequestListItem v-for="request in filteredRequests" v-bind:request="request"
                             v-bind:key="request.id"></RequestListItem>
        </div>
    </section>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import FndSelect from "./form/FndSelect";
    import ListFilter from "./ListFilter";
    import RequestListItem from "./RequestListItem";
    import RequestListItemPendingFund from "./RequestListItemPendingFund";

    import RequestListDto from "../../../app/dto/RequestListDto";
    import RequestListItemDto from "../../../app/dto/RequestListItemDto";
    import {RequestListItemPendingFundDto} from "../../../app/dto/RequestListItemPendingFundDto";

    @Component({
        components: {
            FndSelect,
            ListFilter,
            RequestListItem,
            RequestListItemPendingFund
        }
    })
    export default class RequestList extends Vue {
        @Prop() statusFilterDefault: string;
        @Prop({required: true}) requests: RequestListItemDto[];
        @Prop() pendingRequests: RequestListItemPendingFundDto[];

        public requestList: RequestListDto = new RequestListDto([]);
        public filteredRequests: RequestListItemDto[] = [];
        public statusFilter: string = "all";
        public searchFilter: string = "";
        public sortBy: string = "";
        public isEmpty: boolean = false;

        mounted() {
            this.requestList = new RequestListDto(this.requests);
            this.statusFilter = this.statusFilterDefault ? this.statusFilterDefault : this.statusFilter;
            this._filterItems(this.statusFilter, this.searchFilter, this.sortBy);
        }

        public setStatusFilter(statusFilter: string) {
            this.statusFilter = statusFilter;
            this._filterItems(statusFilter, this.searchFilter, this.sortBy);
        }

        public setSearchFilter(searchFilter: string) {
            this.searchFilter = searchFilter;
            this._filterItems(this.statusFilter, searchFilter, this.sortBy);
        }

        public setSortBy(sortBy: string) {
            this.sortBy = sortBy;
            this._filterItems(this.statusFilter, this.searchFilter, sortBy);
        }

        private _filterItems(statusFilter: string, searchFilter: string, sortBy: string) {
            if (statusFilter.toLowerCase() == "all") {
                this.filteredRequests = this.requestList.getAllRequests(searchFilter, sortBy);
            } else {
                this.filteredRequests = this.requestList.filterByStatus(statusFilter, searchFilter, sortBy);
            }
            this.isEmpty = this.filteredRequests.length <= 0;
        }

    }
</script>

<style></style>
