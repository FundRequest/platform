import Vue from 'vue';
import Utils from '../classes/Utils';

Vue.filter('token', (value: string | number) => {
    return Utils.formatTokenPrice(value);
});

