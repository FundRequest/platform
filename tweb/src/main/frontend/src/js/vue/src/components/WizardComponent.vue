<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import {Wizard} from "../../../app/wizard";
    import {Github, GithubIssue} from "../../../app/github";
    import {TokenInfo} from "../../../app/token-info";
    import {Contracts} from "../../../app/contracts";

    @Component
    export default class WizardComponent extends Vue {

        public githubUrl: string = "";
        public githubIssue: GithubIssue = null;
        public paymentMethod: string = "dapp";
        public supportedTokens: TokenInfo[] = [];
        public selectedToken: string = "";

        mounted() {
            new Wizard(this.$el, () => alert("initate fund transaction"));
        }

        updateUrl(url) {
            Github.getGithubInfo(url).then((res: GithubIssue) => {
                this.githubIssue = res;
                this.githubUrl = url;
                this.$forceUpdate();
                this.updatePossibleTokens(res);
            });
        }

        private updatePossibleTokens(res: GithubIssue) {
            Contracts.getInstance().getPossibleTokens(res.platformId).then((res: TokenInfo[]) => {
                if (res) {
                    this.supportedTokens = res;
                    this.selectedToken = this.supportedTokens[0].address;
                    this.$forceUpdate();
                }
            });
        }
    }

</script>
