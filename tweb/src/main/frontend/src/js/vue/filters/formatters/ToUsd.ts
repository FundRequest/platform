import Utils from '../../../classes/Utils';

export default class ToUsd {
    public static filter(value: string | number, decimals: number = 2) {
        return Utils.formatToUsd(value, decimals);
    }
}