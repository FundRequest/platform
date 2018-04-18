import Vue from 'vue';

import StatisticTile from './components/StatisticTile.vue';
import WizardComponent from './components/WizardComponent.vue';
import RequestList from './components/RequestList.vue';

let v = new Vue({
    el: '#vue-app',
    data: {},
    components: {
        'statistic-tile': StatisticTile,
        'wizard-component': WizardComponent,
        'request-list': RequestList
    },
    mounted() {
        setTimeout(function () {
            document.body.classList.remove('preload');
        }, 500);
    }
});
