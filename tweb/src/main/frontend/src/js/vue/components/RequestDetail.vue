<template>
    <section class="request-details">
        <list-filter
                v-bind:active="faseFilter"
                v-bind:default="faseFilterDefault"
                v-bind:filters="filters"
                v-on:update="setFaseFilter"
        />

        <slot v-bind:faseFilter="faseFilter"></slot>
    </section>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import FndSelect from "./form/FndSelect";
    import ListFilter from "./ListFilter";

    import RequestDto from "../dtos/RequestDto";
    import ListFilterDto from '../dtos/ListFilterDto';

    import VueTimeago from "vue-timeago";

    Vue.use(VueTimeago, {
        name: "Timeago", // Component name, `Timeago` by default
        locale: undefined // Default locale
    });

    @Component({
        components: {
            FndSelect,
            ListFilter
        }
    })
    export default class RequestList extends Vue {
        @Prop() filters: ListFilterDto[];
        @Prop() faseFilterDefault: string;
        @Prop({required: true}) request!: any;

        private _request: RequestDto;
        public faseFilter: string = "all";

        public mounted() {
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

        public setFaseFilter(faseFilter: string) {
            this.faseFilter = faseFilter;
        }

    }
</script>

<style></style>
