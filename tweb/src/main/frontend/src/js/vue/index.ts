import Vue from 'vue';

import FontSizeFit from './components/FontSizeFit.vue';
import RequestDetail from './components/RequestDetail.vue';
import RequestList from './components/RequestList.vue';
import RequestListPendingFunds from './components/RequestListPendingFunds.vue';
import StatisticTile from './components/StatisticTile.vue';
import WizardComponent from './components/WizardComponent.vue';
import StarredLink from './components/StarredLink.vue';

import './filters';
import {EventBus} from './EventBus';
import {DynamicContent} from '../app/DynamicContent';

let v = new Vue({
    el: '#vue-app',
    data: {},
    components: {
        'request-detail': RequestDetail,
        'request-list': RequestList,
        'request-list-pending-funds': RequestListPendingFunds,
        'statistic-tile': StatisticTile,
        'starred-link': StarredLink,
        'wizard-component': WizardComponent,
        'font-size-fit': FontSizeFit,
    },
    mounted() {
        new DynamicContent();
        setTimeout(function () {
            document.body.classList.remove('preload');
        }, 500);
    }
});

window.addEventListener('hashchange', (e: HashChangeEvent) => {
    EventBus.$emit('hashchange');
}, false);