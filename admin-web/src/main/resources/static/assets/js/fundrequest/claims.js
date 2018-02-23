$(document).ready(function () {
  $('#pending-claims').DataTable();

  $('button.approve').on('click', function () {
    let requestClaimId = $(this).data('id');
    submit("/claims/approved", "POST", {
      id: requestClaimId
    })
  });

  $('button.decline').on('click', function () {
    let requestClaimId = $(this).data('id');
    submit("/claims/declined", "POST", {
      id: requestClaimId
    })
  });

  function submit(action, method, values) {
    var form;
    form = $('<form />', {
      action: action,
      method: method,
      style: 'display: none;'
    });
    if (typeof values !== 'undefined' && values !== null) {
      $.each(values, function (name, value) {
        $('<input />', {
          type: 'hidden',
          name: name,
          value: value
        }).appendTo(form);
      });
    }
    form.appendTo('body').submit();
  }

});