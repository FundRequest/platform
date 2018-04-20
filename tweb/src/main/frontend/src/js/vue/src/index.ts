import Vue from 'vue';

import StatisticTile from './components/StatisticTile.vue';
import WizardComponent from './components/WizardComponent.vue';
import RequestList from './components/RequestList.vue';
import Router from 'vue-router';

Vue.use(Router);

let v = new Vue({
    el: '#vue-app',
    data: {
        hash: window.location.hash ? window.location.hash.split('#')[0] : ''
    },
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
