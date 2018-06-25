<template>
	<div class="project-container">
		<div class="project-icon">
			<img v-bind:src="project.logoLocation">
		</div>
		<a v-bind:href="project.projectLink">
			<div class="project-request-details" :style="{ 'background': project.overviewColor }">
				<div class="project-request-name"><h3>{{ project.name }}</h3></div>
				<div class="project-request-description"><p>{{ project.description }}</p></div>
				<hr />
				<div class="requestCountContainer">
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

    @Component
    export default class ProjectCard extends Vue {

		defaultProject: ProjectOverviewDetail = {
			name: "FundRequest",
			description: "Marketplace for Software Development",
			overviewColor: "#000000",
			projectLink: "https://fundrequest.io/requests?",
			logoLocation: "/assets/img/logo-single180.png",
			activeRequests: 5
		};

		@Prop({ 
			default: function() { 
				return {
					name: "FundRequest",
					description: "Marketplace for Software Development",
					overviewColor: "#000000",
					projectLink: "https://fundrequest.io/requests?",
					logoLocation: "/assets/img/logo-single180.png",
					activeRequests: 5
				};
			} 
		}) project: ProjectOverviewDetail

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
			this.project.name = this.project.name || this.defaultProject.name;
			this.project.description = this.project.description || this.defaultProject.description;
			this.project.overviewColor = this.project.overviewColor || this.defaultProject.overviewColor;
			this.project.projectLink = this.project.projectLink || this.defaultProject.projectLink;
			if (!this.project.logoLocation) {
				this.project.logoLocation = this.defaultProject.logoLocation;
			} else if (this.project.logoLocation !== this.defaultProject.logoLocation) {
				this.project.logoLocation = this.project.logoLocation + '?size=150';
			}
			this.project.activeRequests = this.project.activeRequests || this.defaultProject.activeRequests;
		}

		mounted() {
			this._mergeWithDefault();
		}
    }
</script>

<style>

	.project-request-details h3 {
		margin: 0 0 0 0;
		color: white;
	}

	.project-request-details p {
		margin: 0 0 0 0;
		color: white;
	}

	hr {
		margin-top: 0;
		margin-bottom: 0;
		height: 1px;
		width: 80%;
		border: 0;
		background: #FFFFFF;
		align: center;
		clear: both;
	}

	.project-request-name {
		margin-top: 1rem;
		margin-left: 10%;
		text-align: left;
		float: left;
	}

	.project-request-description {
		margin-left: 10%;
		margin-bottom: 0.2rem;
		line-height: 1rem;
		height: 3rem;
		clear: left;
		text-align: left;
		font-size: 13px;
		float: left;
	}

	.requestCountContainer {}

	.requestNumber {
		margin-top: 0.5rem;
		margin-left: 10%;
		margin-right: 0.2rem;

		padding: 0;

		text-align: left;
		font-size: 30px;
		float: left;
	}

	 .requestLabel {
		margin-top: 0.9rem;
		margin-bottom: 0.3rem;
		text-align: left;
		line-height: 0.9rem;
		font-size: 12px;
		white-space: pre-line;
		float: left;
	}

	.project-container {
		margin-left: 10px;
		margin-right: 10px;
		position: relative;
		height: 150px;
		width: 150px;
		background: white;
		border: 1px solid black;
	}
	
	.project-icon {
		height: 100%;
		width: 100%;
	}

	.project-icon img {
		height: inherit;
		width: auto;
	}

	.project-request-details {
		flex: 0 0 auto; 
		margin-right: 3px;

		position: absolute;
		top: 0;
		left: 0;
		height: 100%;
		width: 100%;

		transition: opacity .5s ease;
		border: 1px solid white;
		background: grey;
		opacity: 0;
	}

	.project-request-details:hover {
		opacity: 0.89;
	}

</style>
