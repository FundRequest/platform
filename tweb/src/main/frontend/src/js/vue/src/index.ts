import Vue from 'vue';

import RequestList from './components/RequestList.vue';
import StatisticTile from './components/StatisticTile.vue';
import WizardComponent from './components/WizardComponent.vue';
import Router from 'vue-router';

Vue.use(Router);

let v = new Vue({
    el: '#vue-app',
    data: {
        hash: window.location.hash ? window.location.hash.split('#')[1] : ''
    },
    components: {
        'request-list': RequestList,
        'statistic-tile': StatisticTile,
        'wizard-component': WizardComponent
    },
    mounted() {
        setTimeout(function () {
            document.body.classList.remove('preload');
        }, 500);
    }
});
