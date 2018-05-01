<template>
    <div class="mb-3 d-block">
        <nav class="navbar navbar-expand navbar--filter mb-2">
            <button class="navbar-toggler" type="button" data-toggle="collapse"
                    data-target="#sec-menu" aria-controls="sec-menu"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="sec-menu">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item" v-for="filter in filters" v-bind:class="{active: filter.value === active}">
                        <a v-if="filter.url"
                           class="nav-link" v-bind:href="filter.url" rel="noreferrer noopener" target="_blank"
                           v-html="filter.title">
                            Filter by
                        </a>
                        <a v-if="!filter.url" class="nav-link" v-bind:href="`#${filter.value}`"
                           v-on:click="updateFilter(filter.value)">
                            <span v-html="filter.title"></span>
                            <span class="badge badge--filter" v-if="filter.count > 0">{{filter.count}}</span>
                        </a>
                    </li>
                </ul>
            </div>
        </nav>

        <div class="navbar--filter__description" v-for="filter in filters"
            v-if="filter.value === active && filter.description">
            {{filter.description}}
        </div>
    </div>

</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import ListFilterDto from "../dtos/ListFilterDto";
    import {EventBus} from "../EventBus";
    import {Utils} from "../../../app/Utils";

    @Component
    export default class ListFilter extends Vue {
        @Prop() active: string;
        @Prop() default: string;
        @Prop() filters: ListFilterDto[];

        mounted() {
            EventBus.$on("hashchange", () => {
                this.updateFilter(Utils.getLocationHashValue());
            });
            let hash = Utils.getLocationHashValue();
            this.updateFilter(hash ? hash : this.default ? this.default : this.active);
        }

        public updateFilter(value) {
            if (value.length > 0) {
                let lowercaseValue = value;
                let index = this.filters.findIndex(filter => filter.value.toLowerCase() == lowercaseValue);

                if (index != -1) {
                    this.$emit("update", value);
                }
            }
        }
    }
</script>

<style></style>
