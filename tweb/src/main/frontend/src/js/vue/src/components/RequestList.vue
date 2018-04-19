<template>
    <section class="issue-lists">
        <nav class="navbar navbar-expand navbar--fnd-filter mb-4">
            <button class="navbar-toggler" type="button" data-toggle="collapse"
                    data-target="#sec-menu" aria-controls="sec-menu"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="sec-menu">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item" v-bind:class="{active: statusFilter==='all'}">
                        <a class="nav-link" v-on:click="setStatusFilter('all')">All</a>
                    </li>
                    <li class="nav-item" v-bind:class="{active: statusFilter==='funded'}">
                        <a class="nav-link" v-on:click="setStatusFilter('funded')">Funded</a>
                    </li>
                    <li class="nav-item" v-bind:class="{active: statusFilter==='starred'}">
                        <a class="nav-link" v-on:click="setStatusFilter('starred')">Starred</a>
                    </li>
                    <li class="nav-item" v-bind:class="{active: statusFilter==='claimed'}">
                        <a class="nav-link" v-on:click="setStatusFilter('claimed')">Claimed</a>
                    </li>
                    <li class="nav-item" v-bind:class="{active: statusFilter==='failed'}">
                        <a class="nav-link" v-on:click="setStatusFilter('failed')">Failed</a>
                    </li>
                </ul>
            </div>
        </nav>
        <div class="issue-list__options">
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
                        <select class="form-control"
                                id="list-sort"
                                v-bind:value="sortBy"
                                v-on:input="setSortBy($event.target.value)">
                            <option value="" selected="selected" disabled="disabled">Sort by</option>
                            <option value="title" selected="selected">Title</option>
                            <option value="fnd">FND</option>
                        </select>
                    </div>
                </div>
            </div>
        </div>
        <div class="issue-list__block card">
            <RequestListItem v-for="request in pendingRequests" v-bind:request="request"
                             v-bind:key="request.id"></RequestListItem>
        </div>
        <div class="issue-list__block card">
            <RequestListItem v-for="request in filteredRequests" v-bind:request="request"
                             v-bind:key="request.id"></RequestListItem>
        </div>
    </section>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import RequestListDto from "../../../app/dto/RequestListDto";
    import RequestListItemDto from "../../../app/dto/RequestListItemDto";
    import RequestListItem from "./RequestListItem";

    @Component({
        components: {RequestListItem}
    })
    export default class RequestList extends Vue {
        @Prop() statusFilterDefault: string;
        @Prop({required: true}) requests: RequestListItemDto[];

        public requestList: RequestListDto = new RequestListDto([]);
        public filteredRequests: RequestListItemDto[] = [];
        public statusFilter: string = "all";
        public searchFilter: string = "";
        public sortBy: string = "";

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
        }

        public get pendingRequests(): RequestListItemDto[] {
            return this.requestList.getPendingRequests();
        }

    }
</script>

<style></style>
