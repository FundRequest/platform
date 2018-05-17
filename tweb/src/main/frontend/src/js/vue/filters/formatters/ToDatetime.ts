import Utils from '../../../classes/Utils';

export default class ToDatetime {
    public static filter(value: string) {
        return Utils.formatDatetime(value);
    }
}