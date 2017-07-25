define(function (require) {
  var $ = require('jquery'),
    $addRequestModal = $('#addRequestModal'),
    $showAddRequestModal = $('#showAddRequestModal');

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
  });
});