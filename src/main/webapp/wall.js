function showEditor() {
    var submit = $(".post_submit");
    CKEDITOR.replace('editor');
    $(".post_add").css("display", "none");
    submit.css("display", "block");
    submit.click(function () {
        var data = CKEDITOR.instances.editor.getData();
        hideEditor();
    });
}

function hideEditor() {
    var submit = $(".post_submit");
    $("#cke_editor").replaceWith($('.sample_editor'));
    submit.css("display", "none");
    $(".post_add").css("display", "inline");
}