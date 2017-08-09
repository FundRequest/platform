define(function (require) {
    var $ = require('jquery'),
        $document = $(document),
        datatables = require('datatables.net-bs');

    console.log('should log on all pages');

    $document.on('click', '.watch-link', function(e) {
        e.preventDefault();

        var $el = $(this);
        var $icon = $('.fa', this);
        var watching = $icon.hasClass('fa-star');
        $.post('/requests/' + $el.data('request_id') + '/watchers', {watch: !watching})
            .done(function() {
                $icon
                    .removeClass('fa-star')
                    .removeClass('fa-star-o')
                    .addClass('fa-star' + (watching ? '-o' : ''));
            });
    });
});