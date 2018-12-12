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

    public arkane: PaymentMethod = new PaymentMethod('arkane', 'Arkane', '');

    public static getInstance() {
        if (!PaymentMethods.instance) {
            PaymentMethods.instance = new PaymentMethods();
        }
        return PaymentMethods.instance;
    }
}