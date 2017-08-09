define(function(require) {
    var $ = require('jquery'),
        $document = $(document),
        addRequestModelSelector = '#addRequestModal',
        showAddRequestModalSelector = '#showAddRequestModal',
        requestsTableSelector = '#requests-table';

    $(function() {

        //TODO create js module for loading external content into modals
        $document.on('click', showAddRequestModalSelector, function(e) {
            $addRequestModal.modal('show');
            $.get("/requests/add").done(function(data) {
                $('#addRequestModalContent').html(data);
                $('#technologies').tagsinput('refresh');
            });
        });

        //TODO create js module for ajax submitting forms
        $document.on('click', addRequestModelSelector + ' #btnAddRequest', function(e) {
            e.preventDefault();
            $(this).attr('disabled', 'true');
            $.post('/requests', $('#addRequestForm').serialize())
                .done(function(data, textStatus, jqXHR) {
                    if (jqXHR.status === 204) {
                        location.reload();
                    } else {
                        $('#addRequestModalContent').html(data);
                        $('#technologies').tagsinput('refresh');
                    }
                })
        });
    });
});