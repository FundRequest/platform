<template>
	<div class="col-12 contained">
		<div class="scrolling-wrapper-flexbox">
			<project-card v-for="project in projects" :project="project" v-bind:key="project.name"></project-card>
		</div>
	</div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import ProjectCard from "./ProjectCard.vue";
	import ProjectOverviewDetail from "../models/ProjectOverviewDetail"
	import Utils from "../../classes/Utils";

	@Component({
		components: {
			'project-card': ProjectCard
		}
	})
    export default class ProjectsOverview extends Vue {

		private projectDataURL = "https://raw.githubusercontent.com/FundRequest/platform-metadata/master/projects"
		private projects: Array<ProjectOverviewDetail> = []

		private async _loadProjects(projects) {

			try {
				let projectData = await Utils.getJSON(this.projectDataURL)
				for (var proj in projectData) {
					projects.push({
						name: projectData[proj]['title'],
						description: projectData[proj]['small_description'],
						overviewColor: projectData[proj]['background-color'],
						projectLink: `https://fundrequest.io/requests?fase=open&projects=${proj}`,
						logoLocation: `https://github.com/${proj}.png`,
						activeRequests: 0
					});
				}
			} catch (err) {
				console.log(err);
			}
		}

		mounted() {
			this._loadProjects(this.projects);
		}
    }
</script>

<style>

	.contained {
		margin-top: 20px;
	}

	.scrolling-wrapper-flexbox {
		display: flex;
		flex-wrap: nowrap;
		overflow-x: auto;
		overflow-y: hidden;

		height: 150px;
		width: 100%;
		-webkit-overflow-scrolling: touch;
	}

</style>
