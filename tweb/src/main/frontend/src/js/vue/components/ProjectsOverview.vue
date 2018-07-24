<template>
    <div class="scrolling-wrapper-flexbox">
        <project-card v-for="project in projects" :project="project" v-bind:key="project.name"></project-card>
    </div>
</template>

<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import ProjectCard from "./ProjectCard.vue";
    import ProjectOverviewDetail from "../models/ProjectOverviewDetail";
    import Utils from "../../classes/Utils";
    import {Locations} from '../../classes/Locations';

    @Component({
        components: {
            "project-card": ProjectCard
        }
    })
    export default class ProjectsOverview extends Vue {

        private projectDataURL = "https://raw.githubusercontent.com/FundRequest/platform-metadata/master/projects";
        private projects: Array<ProjectOverviewDetail> = [];

        private async _loadProjects(projects) {

			try {
				let activeRequestCounts = await Utils.getJSON("/requestsActiveCount");
				let projectData = await Utils.getJSON(this.projectDataURL)
				for (var proj in projectData) {
					let activeRequestCount = activeRequestCounts[proj] || 0;
					projects.push({
						name: projectData[proj]['title'],
						description: projectData[proj]['small_description'],
						overviewColor: projectData[proj]['background-color'],
						projectLink: `https://fundrequest.io/requests?fase=open&projects=${proj}`,
						logoLocation: `https://github.com/${proj}.png`,
						activeRequests: activeRequestCount
					});
				}
			} catch (err) {
				console.log(err);
			}
		}

		created() {
			this._loadProjects(this.projects);
		}
    }
</script>

<style lang="scss" scoped>
    .scrolling-wrapper-flexbox {
        display: flex;
        flex-wrap: nowrap;
        overflow-x: auto;
        overflow-y: hidden;

        -webkit-overflow-scrolling: touch;

        &::after {
            display: block;
            content: '';
            padding: .25rem;
        }
    }

</style>
