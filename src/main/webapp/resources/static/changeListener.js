$(document).ready(function () {
    var tempValue = '';
    var input =  $('input');
    input.focus(function () {
        tempValue = $(this).val();
    });
    input.blur(function () {
        if (tempValue != $(this).val()) activateCommit();
    });
});
function activateCommit() {
    $(".commit").removeAttr('disabled')
}
