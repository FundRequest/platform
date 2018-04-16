import Vue from 'vue';

import StatisticTile from './components/StatisticTile.vue';
import WizardComponent from './components/WizardComponent.vue';

let v = new Vue({
    el: '#vue-app',
    data: {name: 'World'},
    components: {
        'statistic-tile': StatisticTile,
        'wizard-component': WizardComponent
    }
});



