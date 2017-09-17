define(['require', 'jquery', 'components/contract'], function(require, $, contract) {
    'use strict';

    // initilize components
    requirejs([
        'components/streams',
        'components/watchers',
        'components/scrollables',
        'components/auto-datatables',
        'components/balance'
    ]);

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

        $document.trigger('fnd.requests.balance.update', data);

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

    $document.on('click', '.btn[data-fund-request]', function(){
        var requestId = $(this).data('fund-request');
        var value = $('[data-fund-request-value="'+requestId+'"]').val();

        contract.fundRequest(requestId, value, function(err, result) {
            if (err) {
                console.error(err);
            } else {
                $('[data-request-balance-message="' + requestId + '"]').html(' (transaction pending)');
                $('[data-role="modal-template"]').modal('hide');
                $document.trigger('fnd.user.balance.update');
                console.log(result);
            }
        });
    });
});