define(function (require) {
  var $ = require('jquery'),
    $addRequestModal = $('#addRequestModal'),
    $showAddRequestModal = $('#showAddRequestModal'),
    $requestsTable = $('#requests-table');

  $(function () {

    //TODO create js module for loading external content into modals
    $showAddRequestModal.on('click', function (e) {
      $addRequestModal.modal('show');
      $.get("/requests/add").done(function (data) {
        $('#addRequestModalContent').html(data);
        $('#technologies').tagsinput('refresh');
      });
    });

    //TODO create js module for ajax submitting forms
    $addRequestModal.on('click', '#btnAddRequest', function (e) {
      e.preventDefault();
      $(this).attr('disabled', 'true');
      $.post('/requests', $('#addRequestForm').serialize())
        .done(function (data, textStatus, jqXHR) {
          if (jqXHR.status === 204) {
            location.reload();
          } else {
            $('#addRequestModalContent').html(data);
            $('#technologies').tagsinput('refresh');
          }
        })
    });

    $requestsTable.on('click', '.watch-link', function (e) {
      e.preventDefault();
    });

    $requestsTable.on('click', '.watch', function (e) {
      var el = $(this);
      var watching = el.hasClass('watch-true');
      $.post('/requests/' + el.data('request_id') + '/watchers', {watch: !watching})
        .done(function () {
          el.removeClass('watch-' + watching);
          el.addClass('watch-' + !watching);
        });
    });

  });
});