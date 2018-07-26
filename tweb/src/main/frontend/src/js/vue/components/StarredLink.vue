<template>
    <i class="request-details__star-link fa-star" v-bind:class="starringCSSClasses" v-on:click="toggleStarred()"></i>
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

		private isStarred: boolean = false;

		mounted() {
			this.isStarred = this.starred;
		}

		get starringCSSClasses() {
			return {
				fa: this.isStarred,
				far: !this.isStarred,
				'-starred': this.isStarred
			};
		}

        public async toggleStarred() {
            const request: RequestDto = await Utils.post(`/requests/${this.id}/watch`);
			this.isStarred = request.starred;
			EventBus.$emit('request-update', request);
        }
    }
</script>

<style></style>
