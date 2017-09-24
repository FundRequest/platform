/* global Vue */

define(['jquery', 'pubsub'], function($, pubsub) {
    'use strict';

    var ComponentStat = {
        props: ['icon', 'val', 'text'],
        template: '#component-stat'
    };

    var ComponentFunded = {
        props: ['val', 'text'],
        template: '#component-funded'
    };

    // create a root instance
    var v_dashboard = new Vue({
        el: '#dashboard',
        components: {
            'component-stat': ComponentStat,
            'component-funded': ComponentFunded
        },
        data: {
            numberOfIssues: {
                icon: 'fa fa-file-code-o',
                text: '# Requests',
                val: 0
            },
            numberOfFunders: {
                icon: 'icon-people',
                text: '# Funders',
                val: 0
            },
            totalAmountFunded: {
                icon: 'icon-fire',
                text: 'Total funded',
                val: 0
            },
            averageFundingPerIssue: {
                icon: 'icon-chart',
                text: 'Average funding',
                val: 0
            },
            percentageFunded: {
                text: 'Funded',
                val: 0
            },
        }
    });

    pubsub.subscribe('fnd/stats/update', function() {
        $.get('/requests/statistics', function(data) {
            v_dashboard.numberOfIssues.val = data.numberOfIssues;
            v_dashboard.numberOfFunders.val = data.numberOfFunders;
            v_dashboard.totalAmountFunded.val = data.totalAmountFunded;
            v_dashboard.averageFundingPerIssue.val = data.averageFundingPerIssue;
            v_dashboard.percentageFunded.val = data.percentageFunded;
        });
    });

    pubsub.publish('fnd/stats/update');

    $(function(){
        $('#dashboard').find('[data-easypiechart]').easyPieChart();
    })
});