<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import {Github, GithubIssue} from "../../../app/github";
    import {TokenInfo} from "../../../app/token-info";
    import {Contracts} from "../../../app/contracts";
    import {PaymentMethod, PaymentMethods} from "../../../app/payment-method";
    import {Utils} from '../../../app/utils';

    @Component
    export default class WizardComponent extends Vue {
        private _activeStep: number = 1;
        private _loading: boolean = false;

        public githubUrl: string = "";
        public githubIssue: GithubIssue = null;
        public supportedTokens: TokenInfo[] = [];
        public selectedToken: TokenInfo = null;
        public panelsHeight: number = 0;

        public paymentMethod: PaymentMethod = PaymentMethods.getInstance().trustWallet;
        public fundAmount: number = 0;

        mounted() {
            this.setActiveStep(1);
        }

        public getClassesPanel(step: number) {
            if (this._activeStep == step) {
                return "movingIn";
            } else if (this._activeStep > step) {
                return "movingOutBackward";
            } else if (this._activeStep < step) {
                return "movingOutForward";
            } else {
                return "";
            }
        }

        public getClassesStep(step: number) {
            if (this._activeStep == step) {
                return "-active";
            } else if (this._activeStep > step) {
                return "-completed";
            } else {
                return "";
            }
        }

        public async setActiveStep(step: number) {
            this._loading = true;
            let valid = true;
            if (step > this._activeStep) {
                let $el: HTMLElement = <HTMLElement> this.$refs[`panelStep${this._activeStep}`];
                let formElements: HTMLElement[] = Array.from($el.querySelectorAll('[data-form-validation]'));
                for (let fieldElement of formElements) {
                    let validations: string[] = fieldElement.dataset.formValidation.split(';');
                    valid = await Utils.validateHTMLElement(fieldElement, validations) && valid;
                }
            }

            if (valid) {
                this._activeStep = step;
                this.panelsHeight = (<HTMLElement>this.$refs[`panelStep${step}`]).clientHeight;
            }

            console.log(valid);
            this._loading = false;
        }

        public get totalAmount() {
            return this.fundAmount;
        }

        public get paymentMethods(): PaymentMethods {
            return PaymentMethods.getInstance();
        }

        public async updateUrl(url): Promise<void> {
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

        public fundWithDapp() {
            throw new Error("Dapp not implemented");
        }

        public fundWithTrust() {
            throw new Error("Trust wallet not implemented");
        }
    }

</script>
