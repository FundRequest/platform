export class PaymentMethod {
    value: string;
    text: string;
    disabledMsg: string;

    constructor(value, text, disabledMsg) {
        this.value = value;
        this.text = text;
        this.disabledMsg = disabledMsg;
    }
}

export class PaymentMethods {
    public static instance: PaymentMethods;

    public trustWallet: PaymentMethod = new PaymentMethod('trustwallet', 'Trust Wallet / QR code (EIP67)', '');
    public dapp: PaymentMethod = new PaymentMethod('dapp', 'MetaMask / Mist / DApp browser', '');

    public wallet: PaymentMethod = new PaymentMethod('wallet', 'Internal Wallet', '(Coming soon)');
    public creditCard: PaymentMethod = new PaymentMethod('credit-card', 'Credit card', '(Coming soon)');
    public paypal: PaymentMethod = new PaymentMethod('paypal', 'PayPal', '(Coming soon)');

    public static getInstance() {
        if (!PaymentMethods.instance) {
            PaymentMethods.instance = new PaymentMethods();
        }
        return PaymentMethods.instance;
    }
}