define(function(require) {
    var $ = require('jquery'),
        _ = require('underscore');


    $(function() {
        //Display backbone and underscore versions
        $('body').append('<div>Its working</div>');
        _.each([1, 2, 3], function(i) {
            $('body').append(i);
        });
    });
});