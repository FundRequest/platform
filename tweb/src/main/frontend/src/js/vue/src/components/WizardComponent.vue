<script lang="ts">
    import {Component, Vue} from "vue-property-decorator";
    import {Github, GithubIssue} from "../../../app/github";
    import {TokenInfo} from "../../../app/token-info";
    import {Contracts} from "../../../app/contracts";
    import {PaymentMethod, PaymentMethods} from "../../../app/payment-method";
    import {Web3} from "../../../app/web3";
    import {Utils} from '../../../app/utils';
    import BigNumber from "bignumber.js";

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
            this.updateDappPaymentMethod();
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

        private async updateDappPaymentMethod() {
            await this.updateDappDisabledMsg();
            if(!PaymentMethods.getInstance().dapp.disabledMsg) {
                this.paymentMethod = PaymentMethods.getInstance().dapp;
            }
        }

        private async updateDappDisabledMsg() {
            let web3 = Web3.getInstance();
            if (web3 && web3.eth && web3.eth.defaultAccount) {
                web3.version.getNetwork(function(err, res) {
                    if(!err) {
                        if(res != '42') {
                            PaymentMethods.getInstance().dapp.disabledMsg = 'Not connected to the correct network.';
                        }
                    }
                });
            } else {
                PaymentMethods.getInstance().dapp.disabledMsg = 'Please initialize your dapp browser correctly, no accounts available.';
            }
        }

        private fund() {
            if (this.paymentMethod === PaymentMethods.getInstance().dapp) {
                this.fundUsingDapp();
            }
        }

        private async fundUsingDapp() {
            let erc20 = await Contracts.getInstance().getErc20Contract(this.selectedToken.address);
            let _web3 = Web3.getInstance();
            let account = _web3.eth.defaultAccount;
            let frContractAddress = Contracts.getInstance().frContractAddress;
            let allowance = (await erc20.allowance(account, frContractAddress)).toNumber();
            console.log(allowance);
            let weiAmount = Number(_web3.toWei(this.fundAmount, 'ether'));
            if (allowance > 0 && allowance < weiAmount) {
                console.log('setting to 0');
                await erc20.approveTx(frContractAddress, 0).send({});
                allowance = 0;
            }
            if (allowance === 0) {
                console.log('You will need to allow the FundRequest contrac to access this token');
                await erc20.approveTx(frContractAddress, new BigNumber('1.157920892e77').minus(1)).send({});
            }
            console.log('funding');
            (await Contracts.getInstance().getFrContract()).fundTx(_web3.fromAscii('GITHUB'), this.githubIssue.platformId, this.selectedToken.address, weiAmount)
                .send({})
                .then((response) => {
                    console.log('response: ' + response);

                })
                .catch((err) => {
                    console.error(err);
                });

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
