<template>
    <section class="request-details">
        <list-filter
                v-bind:active="statusFilter"
                v-bind:default="statusFilterDefault"
                v-bind:filters="filters"
                v-on:update="setStatusFilter"
        />

        <slot v-bind:statusFilter="statusFilter"></slot>
    </section>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import FndSelect from "./form/FndSelect";
    import ListFilter from "./ListFilter";

    import RequestDto from "../dtos/RequestDto";
    import ListFilterDto from '../dtos/ListFilterDto';

    @Component({
        components: {
            FndSelect,
            ListFilter
        }
    })
    export default class RequestList extends Vue {
        @Prop() filters: ListFilterDto[];
        @Prop() statusFilterDefault: string;
        @Prop({required: true}) request!: any;

        private _request: RequestDto;
        public statusFilter: string = "all";

        mounted() {
            this._request = Object.assign(new RequestDto, this.request);
            if(this._request.platform.toUpperCase() == 'GITHUB') {
                this.filters.push(Object.assign(new ListFilterDto(), {
                    value: 'view-on-github',
                    title: `<span class="text-secondary">View on github</span> <i class="fab fa-github"></i>`,
                    description: '',
                    url: `https://github.com/${this._request.owner}/${this._request.repo}/issues/${this._request.issueNumber}`
                }));
            }
        }

        public setStatusFilter(statusFilter: string) {
            this.statusFilter = statusFilter;
        }

    }
</script>

<style></style>
