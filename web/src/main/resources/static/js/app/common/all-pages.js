define(function(require) {
    'use strict';

    // initilize components
    requirejs([
        'components/streams',
        'components/watchers',
        'components/scrollables',
        'components/auto-datatables'
    ]);

    var $ = require('jquery');

    var $document = $(document);

    // submit form and reload page after success
    // TODO make specific for forms, so there is no need to pass the form as data
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

        $('form', $content).on('submit', function(e) {
            e.preventDefault();
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
});