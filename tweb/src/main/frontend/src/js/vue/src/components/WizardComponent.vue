<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import {Wizard} from "../../../app/wizard";
    import {Github, GithubIssue} from "../../../app/github";

    @Component
    export default class WizardComponent extends Vue {

        public githubUrl: string = "";
        public githubIssue: GithubIssue = null;
        public paymentMethod: string = "dapp";

        mounted() {
            new Wizard(this.$el, () => alert("initate fund transaction"));
        }

        updateUrl(url) {
            Github.getGithubInfo(url).then((res: GithubIssue) => {
                this.githubIssue = res;
                this.githubUrl = url;
                this.$forceUpdate();
            });
        }
    }

</script>
