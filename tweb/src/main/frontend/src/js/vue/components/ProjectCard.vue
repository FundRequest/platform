<template>
	<div class="project-container">
		<div class="project-icon">
			<img v-bind:src="project.logoLocation">
		</div>
		<a v-bind:href="project.projectLink">
			<div class="project-request-details" :style="{ 'background': project.overviewColor }">
				<div class="project-request-name"><p><b>{{ project.name }}</b></p></div>
				<div class="project-request-description"><p>{{ project.description }}</p></div>
				<div class="request-count-container">
					<hr />
					<div class="requestNumber"><p>#{{ boundedRequests }}</p></div>
					<div class="requestLabel"><p>active</p><p>requests</p></div>
				</div>
			</div>
		</a>
	</div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
	import ProjectOverviewDetail from "../models/ProjectOverviewDetail"

	const defaultProject: ProjectOverviewDetail = Object.assign(new ProjectOverviewDetail(), {
		name: "FundRequest",
		description: "Marketplace for Software Development",
		overviewColor: "#000000",
		projectLink: "https://fundrequest.io/requests?",
		logoLocation: "/assets/img/logo-single180.png",
		activeRequests: 0
	});

    @Component
    export default class ProjectCard extends Vue {

		@Prop({ default: {...defaultProject} }) project: ProjectOverviewDetail

		get colorStyle() {
			return {
				'background' : this.project.overviewColor
			}
		}

		get boundedRequests() {
			if (this.project.activeRequests > 99) {
				return "99+";
			} else {
				return this.project.activeRequests;
			}
		}

		private _mergeWithDefault() {
			this.project = Object.assign({...defaultProject}, this.project);
			if (this.project.logoLocation !== defaultProject.logoLocation) {
				this.project.logoLocation = this.project.logoLocation + '?size=150';
			}
		}

		mounted() {
			this._mergeWithDefault();
		}
    }
</script>

<style scoped>

	.project-request-details h3 {
		margin: 0;
		color: white;
	}

	.project-request-details p {
		margin: 0;
		color: white;
	}

	hr {
		margin-top: 0;
		margin-bottom: 0;
		height: 1px;
		width: 80%;
		border: 0;
		background: white;
		align: center;
		clear: both;
	}

	.project-request-name {
		margin-top: 1rem;
		margin-left: 1rem;
		text-align: left;
		float: left;
	}

	.project-request-description {
		margin-left: 1rem;
		margin-bottom: 0.2rem;
		line-height: 1rem;
		clear: left;
		text-align: left;
		font-size: 0.8125rem;
		float: left;
	}

	.request-count-container {
		align-self: flex-end;
	}

	.requestNumber {
		margin-top: 0.2rem;
		margin-left: 1rem;
		margin-right: 0.2rem;

		padding: 0;

		text-align: left;
		font-size: 1.875rem;
		float: left;
	}

	 .requestLabel {
		margin-top: 0.6rem;
		margin-bottom: 0.3rem;
		text-align: left;
		line-height: 0.9rem;
		font-size: 0.75rem;
		white-space: pre-line;
		float: left;
	}

	.project-container {
		margin: 0.625rem 0.625rem 0.625rem 0.625rem;
		position: relative;
		min-height: 9.357rem;
		min-width: 9.357rem;
		background: white;
		border: 2px solid #7c7c7c;
		overflow: hidden;
	}
	
	.project-icon {
		padding: 0.625rem;
		height: 100%;
		width: 100%;
	}

	.project-icon img {
		height: inherit;
		width: auto;
		max-width: 100%;
		object-fit: contain;
	}

	.project-request-details {
		flex: 0 0 auto; 
		/* margin-right: 3px;*/

		position: absolute;
		top: 0;
		left: 0;
		height: 100%;
		width: 100%;

		transition: opacity .5s ease;
		background: grey;
		opacity: 0;
	}

	.project-request-details:hover {
		opacity: 0.89;
	}

</style>
