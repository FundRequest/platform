import Utils from '../../../classes/Utils';

export default class ToCrypto {
    public static filter(value: string | number, decimals: number = 2) {
        return Utils.formatToCrypto(value, decimals);
    }
}