/**
 * Initializes the jQuery-slimScroll plugin for all [data-scrollable] items
 *
 * @link https://github.com/rochal/jQuery-slimScroll
 */

define(['require', 'jquery'], function(require, $) {
    'use strict';

    function initScrollables() {
        var $scrollable = $('[data-scrollable]');
        if ($scrollable.length > 0) {
            requirejs(['jQuery-slimScroll'], function() {
                $scrollable.each(function() {
                    var $scroll = $(this);
                    var height = $scroll.data('scrollable');
                    $scroll.slimScroll({
                        height: height || 250
                    })
                });
            });
        }
    }

    $(function() {
        try {
            initScrollables();
        } catch (e) {
            console.error('ERROR initializing scrollable: ', e.message);
        }
    });
});