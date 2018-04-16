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
        public selectedToken: TokenInfo = null;

        mounted() {
            new Wizard(this.$el, () => {
                alert(this.paymentMethod);
            });
        }

        async updateUrl(url): Promise<void> {
            this.githubIssue = await Github.getGithubInfo(url);
            this.githubUrl = url;
            this.$forceUpdate();
            await this.updatePossibleTokens(this.githubIssue);
        }

        private async updatePossibleTokens(res: GithubIssue): Promise<void> {
            if (res != null) {
                let tokens: TokenInfo[] = await Contracts.getPossibleTokens(res.platformId);
                if (tokens) {
                    this.supportedTokens = tokens;
                    this.selectedToken = this.supportedTokens[0];
                    this.$forceUpdate();
                }
            }
        }
    }

</script>
