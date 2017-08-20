/**
 * Create a stream of messages via a websocket.
 *
 * @constructor
 * @param bufferSize Size of buffer containing messages, when having more message in buffer then it drops the first one's
 * @param client Path for Socket
 * @param stream Path of stream
 *
 * @example
 *  var activityStream = new ActivityStream(50, 'fr-ws', '/topic/activities');
 *  var buffer = activityStream.getBuffer().getArr();
 */

define(
    ['require', 'jquery', './CircularBuffer', 'sockjs'],
    function(require, $, CircularBuffer, SockJS) {
        'use strict';

        function ActivityStream(bufferSize, client, stream) {
            var _self = this;
            this.buffer = new CircularBuffer(bufferSize);
            this.socket.client = new SockJS(client);

            this.socket.client.onopen = function() {
                console.log('socket.client open');
            };
            this.socket.client.onclose = function() {
                console.log('socket.client close');
            };

            this.socket.stomp = window.Stomp.over(this.socket.client); // window.Stomp needed when import Stomp via requirejs
            this.socket.stomp.connect({}, function(frame) {
                _self.socket.stomp.subscribe(stream, function(message) {
                    _self.buffer.add(JSON.parse(message.body));
                });
                _self.setConnected(true);
            }, function(err) {
                console.log("Error : " + err);
            });

            this.socket.stomp.debug = function(str){
                // do some debug
                // default this will log the str
                // to disable it, just put in null
                console.log('%c' + str, 'color: #23b7e5');
            }
        }

        ActivityStream.prototype.connected = false;

        ActivityStream.prototype.socket = {};

        ActivityStream.prototype.onclose = function() {
        };

        ActivityStream.prototype.setConnected = function(connected) {
            this.connected = connected;
        };

        ActivityStream.prototype.getBuffer = function() {
            return this.buffer;
        };

        return ActivityStream;
    }
);