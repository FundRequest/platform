import Vue from 'vue';

import RequestDetail from './components/RequestDetail.vue';
import RequestList from './components/RequestList.vue';
import RequestListPendingFunds from './components/RequestListPendingFunds.vue';
import StatisticTile from './components/StatisticTile.vue';
import WizardComponent from './components/WizardComponent.vue';

import './filters';

let v = new Vue({
    el: '#vue-app',
    data: {
        hash: window.location.hash ? window.location.hash.split('#')[1] : ''
    },
    components: {
        'request-detail': RequestDetail,
        'request-list': RequestList,
        'request-list-pending-funds': RequestListPendingFunds,
        'statistic-tile': StatisticTile,
        'wizard-component': WizardComponent
    },
    mounted() {
        setTimeout(function () {
            document.body.classList.remove('preload');
        }, 500);
    }
});
