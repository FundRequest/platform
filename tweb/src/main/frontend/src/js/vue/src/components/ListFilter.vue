<template>
    <div class="mb-5">
        <nav class="navbar navbar-expand navbar--fnd-filter mb-2">
            <button class="navbar-toggler" type="button" data-toggle="collapse"
                    data-target="#sec-menu" aria-controls="sec-menu"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="sec-menu">
                <ul class="navbar-nav mr-auto">
                    <li class="nav-item" v-for="filter in filters" v-bind:class="{active: filter.value === active}">
                        <a class="nav-link" v-bind:href="`#${filter.value}`" v-on:click="$emit('update', filter.value)">
                            {{filter.title}}
                        </a>
                    </li>
                </ul>
            </div>
        </nav>

        <h5 class="section-subtitle text-muted" v-for="filter in filters" v-if="filter.value === active && filter.description">
            <small>{{filter.description}}</small>
        </h5>
    </div>

</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";

    class ListFilter {
        value: string;
        title: string;
        description: string;
    }

    @Component
    export default class RequestListFilter extends Vue {
        @Prop() active: string;
        @Prop() filters: ListFilter[];
    }
</script>

<style></style>
