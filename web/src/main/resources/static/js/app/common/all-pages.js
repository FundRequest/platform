define(function(require) {
    'use strict';

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

    // submit form and reload page after success
    $document.on('fnd.form.submit', function(event, data) {
        event.preventDefault();

        var $form = $(data.form);
        var $content = $(data.content);

        $.post($form.attr('action'), $form.serialize()).done(function(data, textStatus, xhr) {
            if (xhr.status === 204) {
                location.reload();
            } else {
                $document.trigger('fnd.content.refresh', {
                    content: $content,
                    html: data
                });
            }
        }).fail(function(xhr, textStatus, error) {
            console.log(error);
        });
    });

    // refresh content with new html
    $document.on('fnd.content.refresh', function(event, data) {
        var $content = $(data.content);
        $content.html(data.html);

        var $tagsinput = $('[data-role="tagsinput"]', $content);

        if ($tagsinput.length > 0) {
            $tagsinput.tagsinput('refresh');
        }

        $('form', $content).on('submit', function() {
            $document.trigger('fnd.form.submit', {
                content: $content,
                form: this
            });
        });
    });

    // open modal
    $document.on('click', '[data-toggle="modal"]', function(e) {
        var button = this;
        var $modal = $('[data-role="modal-template"]');
        var path = $(button).data('modal-path');

        $modal.modal('show');

        $.get(path).done(function(data) {
            var $content = $('.modal-content', $modal);
            $document.trigger('fnd.content.refresh', {
                content: $content,
                html: data
            });

        }).fail(function(xhr, textStatus, error) {
            console.log(error);
        });
    });
})
;