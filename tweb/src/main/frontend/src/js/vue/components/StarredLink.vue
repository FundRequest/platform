<template>
    <i class="request-details__star-link fa-star" v-bind:class="isStarred" v-on:click="toggleStarred()"></i>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
	import {EventBus} from "../EventBus";
    import RequestDto from "../dtos/RequestDto";
    import Utils from "../../classes/Utils";

    @Component
    export default class StarredLink extends Vue {
        @Prop() starred: boolean;
        @Prop() id: number;

		get isStarred() {
			return {
				fa: this.starred,
				far: !this.starred,
				'-starred': this.starred
			};
		}

        public async toggleStarred() {
            const request: RequestDto = await Utils.post(`/requests/${this.id}/watch`);
			this.starred = request.starred;
			EventBus.$emit('request-update', request);
        }
    }
</script>

<style></style>
