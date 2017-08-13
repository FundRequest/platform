/**
 *  Initialize all [data-stream].
 *  Create a stream of messages via a websocket.
 *
 * @example
 *  <div data-stream="stream-location-under-topic">
 *      <div v-for="message in messages">
 *          <div>{{message.prop1}}</div>
 *          <div>{{message.prop2}}</div>
 *      </div>
 *  </div>
 *
 * @notes
 *  VueTimeago available
 *
 * @link: https://github.com/egoist/vue-timeago
 */

define(function(require) {
    'use strict';

    function initStreams() {
        var $streams = $('[data-stream]');

        if ($streams.length > 0) {
            requirejs(['components/ActivityStream', 'vuejs', 'vue-timeago/vue-timeago'], function(ActivityStream, Vue, VueTimeago) {
                Vue.use(VueTimeago, {
                    name: 'timeago', // component name, `timeago` by default
                    locale: 'en-US',
                    locales: {
                        'en-US': require('json!vue-timeago/locales/en-US.json')
                    }
                });

                $streams.each(function() {
                    var stream = this;
                    var $stream = $(stream);
                    var activityStream = new ActivityStream(50, 'fr-ws', '/topic/' + $stream.data('stream'));

                    var buffer = activityStream.getBuffer().getArr();

                    var app = new Vue({
                        el: stream,
                        data: {
                            messages: buffer
                        },
                        computed: {
                            reverseMessages: function() {
                                return this.messages.slice().reverse();
                            }
                        }
                    });
                });
            });
        }
    }

    $(function() {
        try {
            initStreams();
        } catch (e) {
            console.error('ERROR initializing streams: ', e.message);
        }
    })
});