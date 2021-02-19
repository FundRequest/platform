<script lang="ts">
    import {Component, Prop, Vue} from "vue-property-decorator";
    import {ArkaneConnect, EthereumTransactionRequest, Signer, SignMethod, Wallet} from "@arkane-network/arkane-connect"
    import QrcodeVue from "qrcode.vue";
    import {VMoney} from "v-money";

    import Github, {GithubIssue} from "../../classes/Github";
    import {TokenInfo} from "../../classes/token-info";
    import {Contracts} from "../../classes/contracts";
    import {PaymentMethod, PaymentMethods} from "../../classes/payment-method";
    import {Web3x} from "../../classes/Web3x";
    import Utils from "../../classes/Utils";
    import Alert from "../../classes/Alert";
    import Faq from "./Faq";
    import BigNumber from "bignumber.js";
    import {PendingFundCommand} from "../models/PendingFundCommand";

    @Component({
        components: {
            QrcodeVue,
            Faq
        },
        directives: {
            money: VMoney
        }
    })
    export default class WizardComponent extends Vue {
        private _activeStep: number = 1;
        private _loading: boolean = false;
        private _network: string;

        public githubUrl: string = "";
        public githubIssue: GithubIssue = null;
        public supportedTokens: TokenInfo[] = [];
        public selectedToken: TokenInfo = null;
        public wallets: Wallet[] = [];
        public selectedWallet: Wallet = null;
        public panelsHeight: number = 0;
        public stepTitlesHeight: number = 0;
        public approveInfoModalActive: boolean = false;

        public currentAllowance: number = 0;
        public currentFundAmount: number = 0;
        public errorMessages: { fundAmount: string } = {fundAmount: ""};

        public paymentMethod: PaymentMethod = PaymentMethods.getInstance().arkane;
        public fundAmount: string = "";
        public description: string = "";
        private connect: ArkaneConnect;

        @Prop() public arkanetoken: string;
        @Prop() public arkaneEnvironment: string;
        @Prop() public chain: string;

        public moneyConfig = {
            decimal: ".",
            thousands: ",",
            prefix: "",
            suffix: "",
            precision: 2,
            masked: true,
            allowBlank: true
        };

        mounted() {
            this.githubUrl = (this.$refs.requestUrl as HTMLElement).dataset.value;
            if (this.githubUrl) {
                this.updateUrl(this.githubUrl);
            }
            let metaNetwork = document.head.querySelector("[name=\"ethereum:network\"]");
            this._network = metaNetwork ? metaNetwork.getAttribute("content") : "";
            this.updateDappPaymentMethod();
            this.gotoStep(1);
            if (this.arkanetoken) {
                this.connect = new ArkaneConnect('Arkane', {
                    environment: this.arkaneEnvironment,
                    signUsing: SignMethod.POPUP,
                    bearerTokenProvider: () => this.arkanetoken
                });
                this.connect.api.getWallets().then(x => {
                    this.wallets = x;
                    this.selectedWallet = this.wallets[0];
                }).catch(x => console.log(x));
            }
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

        public getReadableAddress(address: string): string {
            return address.substring(0, 7) + "..." + address.substring(address.length - 5, address.length);
        }

        public async gotoStep(step: number, onlyCompleted: boolean = false) {
            this._loading = true;
            let valid = true;
            if (step > this._activeStep && !onlyCompleted) {
                let $el: HTMLElement = <HTMLElement> this.$refs[`panelStep${this._activeStep}`];
                let formElements: HTMLElement[] = Array.from($el.querySelectorAll("[data-form-validation]"));
                for (let fieldElement of formElements) {
                    let validations: string[] = fieldElement.dataset.formValidation.split(";");
                    valid = await Utils.validateHTMLElement(fieldElement, validations) && valid;
                }

                if (valid && step == 3) {
                    let element = formElements.find((el: HTMLInputElement) => el.name == "fundAmount") as HTMLInputElement;
                    if (this.fundAmountValue > 0) {
                        if (this.paymentMethod == PaymentMethods.getInstance().arkane) {
                            valid = await this._validateFundAmountBalance(element);
                        }
                    } else {
                        this.errorMessages.fundAmount = `You have to fund more then 0 ${this.selectedToken.symbol}`;
                        Utils.setElementInvalid(element);
                        valid = false;
                    }
                } else {
                    this.errorMessages.fundAmount = `Please enter a valid number, e.g.: 120.00`;
                }
                this.$forceUpdate();
            }

            if (valid) {
                if (onlyCompleted && step < this._activeStep || !onlyCompleted) {
                    this._activeStep = step;
                    this.panelsHeight = (<HTMLElement>this.$refs[`panelStep${step}`]).clientHeight;
                    this.stepTitlesHeight = (<HTMLElement>this.$refs[`stepTitle${step}`]).clientHeight;
                }
            }
            this._loading = false;
        }

        private async _validateFundAmountBalance(element: HTMLInputElement): Promise<boolean> {
            let valid = true;
            if (this.paymentMethod == PaymentMethods.getInstance().arkane) {
                let balance = await this.connect.api.getTokenBalance(this.selectedWallet.id, this.selectedToken.address);
                if (this.fundAmountValue > balance.balance) {
                    this.errorMessages.fundAmount = `Your ${this.selectedToken.symbol} balance is to low.`;
                    Utils.setElementInvalid(element);
                    valid = false;
                }
            }
            return valid;
        }

        private async updateDappPaymentMethod() {
            await this.updateDappDisabledMsg();
            this.paymentMethod = PaymentMethods.getInstance().arkane;
        }

        private async updateDappDisabledMsg() {
            let web3 = await Web3x.getInstance();
            if (web3 && web3.eth && web3.eth.defaultAccount) {
                await new Promise((resolve, reject) => {
                    web3.version.getNetwork((err, res) => {
                        if (!err && res != this._network) {
                            PaymentMethods.getInstance().arkane.disabledMsg = "Not connected to main network.";
                        }
                        resolve("not connected");
                    });
                });
            } else {
                PaymentMethods.getInstance().arkane.disabledMsg = "DApp not available, no accounts available.";
            }
        }

        private async fund() {
            switch (this.paymentMethod) {
                case PaymentMethods.getInstance().arkane:
                    try {
                        let result = await this.fundUsingArkane();
                        if (result.length > 0) {
                            if (Utils.openedByBrowserplugin) {
                                document.dispatchEvent(new CustomEvent("browserplugin.to.extension.fnd.FUND_SUCCESS", {
                                    detail: {
                                        done: true,
                                        body: result,
                                        redirectLocation: "/user/requests"
                                    }
                                }));
                            } else {
                                window.location.href = "/user/requests";
                            }
                        }
                    } catch (err) {
                        Alert.error(`Something went wrong during funding, please try again. <br/> If the problem remains, <a href="https://help.fundrequest.io">please contact the FundRequest team</a>.`);
                    }
                    break;
                default:
                    break;
            }
        }

        private get fundAmountValue(): number {
            return Number(this.fundAmount.replace(/,/g, ""));
        }

        private async fundUsingArkane(): Promise<string> {
            Utils.showLoading();
            let frContractAddress = Contracts.frContractAddress;
            let erc20 = await Contracts.getErc20Contract(this.selectedToken.address);
            let decimals = this.selectedToken.decimals;
            this.currentAllowance = (await erc20.allowance(this.selectedWallet.address, frContractAddress)).toNumber();
            this.currentFundAmount = Number(this.fundAmountValue * Math.pow(10, decimals));
            Utils.hideLoading();

            if (!this.approveInfoModalActive && this.currentAllowance < this.currentFundAmount) {
                this.showApproveInfoModal();
                return "";
            }
            else {
                Utils.showLoading();
                if (this.currentAllowance > 0 && this.currentAllowance < this.currentFundAmount) {
                    // setting to 0
                    await this.approveErc20(erc20.address, this.selectedWallet, frContractAddress, new BigNumber("0"));
                    this.currentAllowance = 0;
                }
                if (this.currentAllowance === 0) {
                    // You will need to allow the FundRequest contract to access this token
                    await this.approveErc20(erc20.address, this.selectedWallet, frContractAddress, Utils.biggestNumber());
                }
                try {
                    let response = <any>(await this.fundInContract(frContractAddress, this.selectedWallet, "GITHUB", this.githubIssue.platformId, this.selectedToken.address, new BigNumber("" + this.currentFundAmount)));
                    let pendingFundCommand = new PendingFundCommand();
                    pendingFundCommand.transactionId = response.result.transactionHash;
                    pendingFundCommand.amount = this.fundAmountValue.toString();
                    pendingFundCommand.description = this.description;
                    pendingFundCommand.fromAddress = await this.selectedWallet.address;
                    pendingFundCommand.tokenAddress = this.selectedToken.address;
                    pendingFundCommand.platform = this.githubIssue.platform;
                    pendingFundCommand.platformId = this.githubIssue.platformId;
                    await Utils.postJSON(`/rest/pending-fund`, pendingFundCommand);
                    return pendingFundCommand.transactionId;
                } catch (e) {
                    if (!e.status || e.status !== 'ABORTED') {
                        throw e;
                    }
                    return Promise.resolve("");
                }
                finally {
                    Utils.hideLoading();
                }
            }
        }

        private async approveErc20(tokenContractAddress: string, wallet: Wallet, spender: string, amount: BigNumber) {
            const signer: Signer = this.connect.createSigner();
            try {
                let req = new EthereumTransactionRequest();
                req.to = tokenContractAddress;
                req.type = this.chain.toLowerCase() === 'bsc' ? 'BSC_TRANSACTION' : 'ETH_TRANSACTION';
                req.walletId = wallet.id;
                req.value = 0;
                req.data = Contracts.encodeErc20ApproveFunction(spender, amount);
                await signer.executeNativeTransaction(req);
            } finally {
                if (this.connect.isPopupSigner(signer)) {
                    signer.closePopup();
                }
            }
        }

        private async fundInContract(frContractAddress: string, wallet: Wallet, platform: string, platformId: string, token: string, amount: BigNumber) {
            const signer: Signer = this.connect.createSigner();
            try {
                let req = new EthereumTransactionRequest();
                req.to = frContractAddress;
                req.type = this.chain.toLowerCase() === 'bsc' ? 'BSC_TRANSACTION' : 'ETH_TRANSACTION';
                req.walletId = wallet.id;
                req.value = 0;
                req.data = Contracts.encodeFundFunction(platform, platformId, token, amount);
                return await signer.executeNativeTransaction(req);
            } finally {
                if (this.connect.isPopupSigner(signer)) {
                    signer.closePopup();
                }
            }
        }

        public showApproveInfoModal() {
            Utils.modal.open(<HTMLElement>this.$refs.approveInfoModal, () => {
                this.hideApproveInfoModal();
            });
            this.approveInfoModalActive = true;
            this._fadeoutPage();
        }

        public hideApproveInfoModal() {
            Utils.modal.close(<HTMLElement>this.$refs.approveInfoModal);
            this.approveInfoModalActive = false;
            this._fadeinPage();
        }

        public get totalAmount() {
            return this.fundAmount;
        }

        public get totalAmountValue() {
            return this.fundAmountValue;
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

        private _fadeoutPage() {
            (<HTMLElement>this.$refs.panels).style.opacity = "0.5";
            (<HTMLElement>this.$refs.panels).style.pointerEvents = "none";
            (<HTMLElement>this.$refs.faq).style.opacity = "0.5";
            (<HTMLElement>this.$refs.faq).style.pointerEvents = "none";
        }

        private _fadeinPage() {
            (<HTMLElement>this.$refs.panels).style.opacity = "";
            (<HTMLElement>this.$refs.panels).style.pointerEvents = "";
            (<HTMLElement>this.$refs.faq).style.opacity = "";
            (<HTMLElement>this.$refs.faq).style.pointerEvents = "";
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
