<template>
    <section class="request-list">
        <list-filter
                v-bind:active="listFilter.fase"
                v-bind:default="faseFilterDefault"
                v-bind:filters="filters"
                v-on:update="setFaseFilter"
        />

        <slot v-bind:faseFilter="listFilter.fase"></slot>

        <div class="request-list__options" v-if="!isEmpty">
            <div class="row">
                <div class="col-12 col-md-3 col-lg-3">
                    <div class="md-form">
                        <input class="form-control form-control--search" id="list-search" type="search"
                               v-bind:value="listFilter.search"
                               v-on:input="setSearchFilter($event.target.value)" />
                        <label for="list-search"><span>Search</span><i class="fa fa-search"></i></label>
                    </div>
                </div>
                <div class="col-12 col-md-3 col-lg-2">
                    <div class="md-form" v-if="projects && projects.length > 0">
                        <v-select
                                v-bind:value="listFilter.project"
                                v-bind:options="projects"
                                v-bind:class="{filled: listFilter.project && listFilter.project.length > 0}"
                                v-on:input="setProjectFilter"
                                id="projects" inputId="projects"></v-select>
                        <label for="projects"><span>Projects</span><i class="far fa-filter"></i></label>
                    </div>
                </div>
                <div class="col-12 col-md-3 col-lg-3">
                    <div class="md-form" v-if="technologies && technologies.length > 0">
                        <v-select
                                v-bind:value="listFilter.tech"
                                v-bind:options="technologiesSelect"
                                v-bind:class="{filled: listFilter.tech && listFilter.tech.length > 0}"
                                v-on:input="setTechFilter"
                                id="technologies" inputId="technologies" multiple></v-select>
                        <label for="technologies"><span>Technologies</span><i class="far fa-filter"></i></label>
                    </div>
                </div>
                <div class="col-12 col-md-3 col-lg-2 offset-lg-2">
                    <div class="md-form">
                        <fnd-select v-bind:id="'list-sort'"
                                    v-bind:value="sortBy"
                                    v-bind:options="sorting"
                                    v-on:input="setSortBy">
                            <option v-bind:value="null" selected="selected" disabled="disabled">SORT BY</option>
                        </fnd-select>
                    </div>
                </div>
            </div>
        </div>
        <div class="request-list__block card" v-if="!hasNoResults">
            <request-list-item v-for="request in filteredRequests" v-bind:request="request"
                               v-bind:key="request.id"></request-list-item>
        </div>
        <div v-bind:class="{'mt-5': isEmpty}" v-if="hasNoResults">
            <div class="request-list__block request-list__block--non-found card">
                <div class="card-body text-center">
                    <div>
                        <img src="/assets/img/unicorn-gray.png"
                             alt="nothing found, gray unicorn" />
                    </div>
                    <h3>Oh snap!</h3>
                    <p>Not a single request found.</p>
                    <p>Try to search with a different filter.</p>
                </div>
            </div>
        </div>

    </section>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import vSelect from "vue-select";
    import FndSelect from "./form/FndSelect";
    import ListFilter from "./ListFilter";
    import RequestListItem from "./RequestListItem";

    import RequestListModel from "../models/RequestList";
    import RequestListFilter from "../models/RequestListFilter";
    import RequestDto from "../dtos/RequestDto";
    import ListFilterDto from "../dtos/ListFilterDto";

    @Component({
        components: {
            FndSelect,
            ListFilter,
            RequestListItem,
            vSelect
        }
    })
    export default class RequestList extends Vue {
        @Prop() filters: ListFilterDto[];
        @Prop() faseFilterDefault: string;
        @Prop() technologies: string[];
        @Prop() projects: string[];
        @Prop({required: true}) requests: RequestDto[];

        public sorting: Array<{ title: string, value: { value: string, asc: boolean } }> = [{
            title: "Funding",
            value: {value: "funding", asc: true}
        }, {
            title: "Last Modified",
            value: {value: "lastModifiedDate", asc: false}
        }, {
            title: "Title",
            value: {value: "title", asc: true}
        }];

        public requestList: RequestListModel = new RequestListModel([]);
        public filteredRequests: RequestDto[] = [];
        public sortBy: { value: string, asc: boolean } = null;
        public hasNoResults: boolean = false;
        public isEmpty: boolean = false;
        public technologiesSelect: string[] = [];

        public listFilter: RequestListFilter = Object.assign(new RequestListFilter(), {
            search: null,
            tech: [],
            project: null,
            fase: "all"
        });

        mounted() {
            this.sortBy = this.sorting[1].value;
            this.requestList = new RequestListModel(this.requests);
            this._filterItems(this.listFilter, this.sortBy);
            this.technologiesSelect = this.technologies.sort();
        }

        public setFaseFilter(fase: string) {
            let filter: RequestListFilter = this.listFilter;
            filter.fase = fase;
            this.listFilter = filter;
            this._filterItems(filter, this.sortBy);
        }

        public setSearchFilter(search: string) {
            let filter: RequestListFilter = this.listFilter;
            filter.search = search;
            this.listFilter = filter;
            this._filterItems(filter, this.sortBy);
        }


        public setTechFilter(tech: string[]) {
            let filter: RequestListFilter = this.listFilter;
            filter.tech = tech;
            this.listFilter = filter;
            this._filterItems(filter, this.sortBy);
        }


        public setProjectFilter(project: string) {
            let filter: RequestListFilter = this.listFilter;
            filter.project = project;
            this.listFilter = filter;
            this._filterItems(filter, this.sortBy);
        }

        public setSortBy(sortBy: { value: string, asc: boolean }) {
            this.sortBy = sortBy;
            this._filterItems(this.listFilter, sortBy);
        }

        private _setIsEmpty(isEmpty: boolean) {
            this.hasNoResults = isEmpty;
            this.isEmpty = !this.listFilter.isFiltered && isEmpty;
        }

        private _filterItems(filter: RequestListFilter, sortBy: { value: string, asc: boolean }) {
            this.filteredRequests = this.requestList.getRequests(filter, sortBy);
            this._setIsEmpty(this.filteredRequests.length <= 0);
        }

    }
</script>

<style></style>
