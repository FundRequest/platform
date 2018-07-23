import Vue from 'vue';
import * as $ from 'jquery';

import Faq from './components/Faq.vue';
import Faucet from './components/Faucet.vue';
import FontSizeFit from './components/FontSizeFit.vue';
import RequestDetail from './components/RequestDetail.vue';
import RequestList from './components/RequestList.vue';
import RequestListPendingFunds from './components/RequestListPendingFunds.vue';
import StatisticTile from './components/StatisticTile.vue';
import WizardComponent from './components/WizardComponent.vue';
import StarredLink from './components/StarredLink.vue';
import ProjectsOverview from './components/ProjectsOverview.vue';

import {EventBus} from './EventBus';
import {DynamicContent} from '../app/DynamicContent';

let v = new Vue({
    el: '#vue-app',
    data: {},
    components: {
        'faq': Faq,
        'faucet': Faucet,
        'request-detail': RequestDetail,
        'request-list': RequestList,
        'request-list-pending-funds': RequestListPendingFunds,
        'statistic-tile': StatisticTile,
        'starred-link': StarredLink,
        'wizard-component': WizardComponent,
        'font-size-fit': FontSizeFit,
        'projects-overview': ProjectsOverview
    },
    mounted() {
        new DynamicContent();

        $('#vue-app').find('[data-toggle="tooltip"]').tooltip();

        setTimeout(function () {
            document.body.classList.remove('preload');
        }, 500);
    }
});

window.addEventListener('hashchange', (e: HashChangeEvent) => {
    EventBus.$emit('hashchange');
}, false);