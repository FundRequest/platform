<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import {Wizard} from "../../../app/wizard";
    import {Github, GithubIssue} from "../../../app/github";
    import {TokenInfo} from "../../../app/token-info";
    import {Contracts} from "../../../app/contracts";
    import {PaymentMethod, PaymentMethods} from "../../../app/payment-method";

    @Component
    export default class WizardComponent extends Vue {

        public githubUrl: string = "";
        public githubIssue: GithubIssue = null;
        public supportedTokens: TokenInfo[] = [];
        public selectedToken: TokenInfo = null;

        public paymentMethod: PaymentMethod = PaymentMethods.getInstance().trustWallet;
        public fundAmount: number = 0;

        mounted() {
            new Wizard(this.$el, () => {
                alert(this.paymentMethod);
            });
        }

        public get totalAmount() {
            return this.fundAmount;
        }

        public get paymentMethods(): PaymentMethods {
            return PaymentMethods.getInstance();
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
