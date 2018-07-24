<template>
    <div class="project-container">
        <div class="project-icon">
            <img v-bind:src="projectDetail.logoLocation">
        </div>
        <a v-bind:href="projectDetail.projectLink">
            <div class="project-request-details" :style="{ 'background': projectDetail.overviewColor }">
                <div class="project-request-name"><strong>{{ projectDetail.name }}</strong></div>
                <div class="project-request-description">{{ projectDetail.description }}</div>
                <div class="project-request-count-container">
                    <div>
                        <div class="project-request-number">#{{ boundedRequests }}</div>
                        <div class="project-request-label">
                            <div>active</div>
                            <div>requests</div>
                        </div>
                    </div>
                </div>
            </div>
        </a>
    </div>
</template>

<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import ProjectOverviewDetail from "../models/ProjectOverviewDetail";

    @Component
    export default class ProjectCard extends Vue {
        @Prop() project!: ProjectOverviewDetail;

        public projectDetail: ProjectOverviewDetail = Object.assign(new ProjectOverviewDetail(), {
            name: "FundRequest",
            description: "Marketplace for Software Development",
            overviewColor: "#000000",
            projectLink: "https://fundrequest.io/requests?",
            logoLocation: "/assets/img/logo-single180.png",
            activeRequests: 0
        });

        public get boundedRequests() {
            if (this.projectDetail.activeRequests > 99) {
                return "99+";
            } else {
                return this.projectDetail.activeRequests;
            }
        }

        created() {
            this.projectDetail = Object.assign(this.projectDetail, this.project);
            this.projectDetail.logoLocation = this.projectDetail.logoLocation + "?size=118";
        }
    }
</script>

<style lang="scss" scoped>

    .project-container {
        position: relative;
        margin: 0.625rem;
        min-height: 9.357rem;
        min-width: 9.357rem;
        background: white;
        border: 2px solid #7c7c7c;
        display: flex;
        align-items: stretch;

        > {
            max-width: 100%;
        }
    }

    .project-request {
        &-details {
            color: #ffffff;

            h3 {
                margin: 0;
                color: white;
                word-break: break-word;
            }

            &:hover {
                opacity: 0.89;
            }

            position: relative;
            height: 100%;
            padding: 1rem;
            display: flex;
            flex-direction: column;
            align-items: stretch;
            justify-content: flex-start;
            z-index: 1;

            transition: opacity .2s ease-out;
            background: #cccccc;
            opacity: 0;
        }

        &-description {
            line-height: 1rem;
            text-align: left;
            font-size: 0.8125rem;
            word-break: break-word;
        }

        &-count-container {
            display: flex;
            flex-direction: column;
            justify-content: flex-end;
            flex-grow: 100;

            &::before {
                content: '';
                display: block;
                height: 1px;
                width: 80%;
                border: 0;
                margin: .5rem auto;
                background: white;
            }

            > div {
                display: flex;
            }
        }

        &-number {
            line-height: 1.5rem;
            font-size: 1.875rem;
        }

        &-label {
            margin-left: .25rem;
            line-height: .75rem;
            font-size: 0.75rem;
            white-space: pre-line;
        }
    }

    .project-icon {
        position: absolute;
        padding: 1rem;
        z-index: 0;
        top: 0;
        left: 0;
        height: 100%;
        width: 100%;
        img {
            height: 7.375rem;
            width: 7.375rem;
            max-width: 100%;
            object-fit: contain;
        }
    }

</style>
