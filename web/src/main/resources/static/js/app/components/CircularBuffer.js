/**
 * Creates an array of a specific size, when growing larger it drops the first items.
 *
 * @constructor
 * @param size Size of buffer array
 *
 * @example
 *  var circularBuffer = new CircularBuffer(50);
 *  circularBuffer.add(item);
 *  circularBuffer.addArray([item2, item3]);
 *  console.log(circularBuffer.getArr());
 */

define(function() {
    'use strict';

    function CircularBuffer(size) {
        this.size = size;
        this.arr = [];
    }

    CircularBuffer.prototype = {
        size: function() {
            return this.size;
        },
        add: function(a) {
            if (this.arr.length >= this.size) {
                this.arr.shift();
            }
            this.arr.push(a);
        },
        addArray: function(arr) {
            for (var i = 0; i < arr.length; i++) {
                this.add(arr[i]);
            }
        },
        newlineStr: function() {
            var str = '';
            for (var i = 0; i < this.arr.length; i++) {
                str = str + this.arr[i] + "\n";
            }
            return str;
        },
        getArr: function() {
            return this.arr;
        }
    };

    return CircularBuffer;
});
