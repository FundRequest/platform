define(function() {
    'use strict';

    var $document = $(document);

    $document.on('click', '.watch-link', function(e) {
        e.preventDefault();

        var $el = $(this);
        var $icon = $('.fa', this);
        var isWatching = $icon.hasClass('fa-star');
        var requestId = $el.data('request-id');
        $.post('/requests/' + requestId + '/watchers', {watch: !isWatching})
            .done(function() {
                $document.trigger('fnd.request.updated', {requestId: requestId});
            });
    });

    $document.on('fnd.request.updated', function(event, data) {
        var requestId = data.requestId;
        var $requestWatchers = $('[data-request-watchers="' + requestId + '"]');

        if ($requestWatchers.length > 0) {
            $requestWatchers.each(function() {
                var $this = $(this);
                var url = $this.data('fragment');

                $.get(url, function(data) {
                    var $html = $(data);
                    var isTooltip = $this.data('toggle') === "tooltip";
                    if (isTooltip) {
                        $this.tooltip('destroy');
                        $html.tooltip({container: 'body'});
                    }
                    $this.replaceWith($html);
                });
            });
        }
    });
});