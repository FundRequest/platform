<template>
    <a href="#star" class="request-details__star-link" v-bind:class="{ '-starred': isStarred}" v-on:click="toggleStarred()">
        <i class="fa fa-star"></i>
        <i class="far fa-star"></i>
    </a>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import RequestDto from "../dtos/RequestDto";
    import Utils from "../../classes/Utils";

    @Component
    export default class StarredLink extends Vue {
        @Prop() starred: boolean;
        @Prop() id: number;

        public isStarred: boolean = false;

        public mounted() {
            this.isStarred = this.starred;
        }

        public async toggleStarred() {
            const request: RequestDto = await Utils.post(`/requests/${this.id}/watch`);
            this.isStarred = request.starred;
        }
    }
</script>

<style></style>
