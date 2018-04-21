import Vue from 'vue';
import {Utils} from '../../app/Utils';

Vue.filter('token', (value: string | number) => {
    return Utils.formatTokenPrice(value);
});

