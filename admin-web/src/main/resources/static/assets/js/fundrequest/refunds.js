$(document).ready(function () {
  $('#pending-refunds').DataTable();
  $('#failed-refunds').DataTable();

  $('button.approve').on('click', function () {
    let refundRequestId = $(this).data('id');
    submit("/refunds/" + refundRequestId, "POST", {
      action: 'APPROVE'
    })
  });

  $('button.decline').on('click', function () {
    let refundRequestId = $(this).data('id');
    submit("/refunds/" + refundRequestId, "POST", {
      action: 'DECLINE'
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