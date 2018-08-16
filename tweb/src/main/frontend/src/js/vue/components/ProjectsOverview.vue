<template>
    <div class="scrolling-wrapper">
        <div class="scrolling-wrapper-flexbox">
            <project-card v-for="project in projects" :project="project" v-bind:key="project.name"></project-card>
        </div>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import ProjectCard from "./ProjectCard.vue";
    import ProjectOverviewDetail from "../models/ProjectOverviewDetail";
    import Utils from "../../classes/Utils";
    import {Locations} from "../../classes/Locations";
    import RequestDto from '../dtos/RequestDto';

    @Component({
        components: {
            "project-card": ProjectCard
        }
    })
    export default class ProjectsOverview extends Vue {
        @Prop({required: true}) requests: RequestDto[];

        private projectDataURL = "https://raw.githubusercontent.com/FundRequest/platform-metadata/master/projects";
        private projects: Array<ProjectOverviewDetail> = [];

        private async _loadProjects(projects) {
            try {
                let projectData = await Utils.getJSON(this.projectDataURL);
                for (let project in projectData) {
                    if (projectData.hasOwnProperty(project)) {
                        projects.push(Object.assign(new ProjectOverviewDetail(), {
                            name: projectData[project]["title"],
                            description: projectData[project]["small_description"],
                            overviewColor: projectData[project]["background-color"],
                            projectLink: `${Locations.requests}?fase=open&project=${project}`,
                            logoLocation: `https://github.com/${project}.png`,
                            activeRequests: this.requests.filter((request: RequestDto) => request.owner.toLowerCase() == project.toLowerCase()).length
                        }));
                    }
                }
            } catch (e) {
                console.error(e);
            }
        }

        created() {
            this._loadProjects(this.projects);
        }
    }
</script>

<style lang="scss" scoped>
    .scrolling-wrapper {
        width: 100%;
        overflow: scroll;
    }

    .scrolling-wrapper-flexbox {
        display: flex;
        flex-wrap: nowrap;
        justify-content: center;
        min-width: min-content;

        -webkit-overflow-scrolling: touch;

        &::after {
            display: block;
            content: '';
            padding: .25rem;
        }
    }

</style>
