function showEditor() {
    var submit = $(".post_submit");
    try{
        var editor = CKEDITOR.replace('editor');
        /*editor.config.autoParagraph = false;*/
        CKEDITOR.instances.editor.setData('<p></p>');
    }catch (e) {
        /*CKEDITOR.instances.editor.updateElement();
        CKEDITOR.replace('editor');*/
    }

    $(".post_add").css("display", "none");
    submit.css({"display":"inline"});
    submit.click(function () {
        var data = CKEDITOR.instances.editor.getData();
        hideEditor();

        $(".posts").prepend('<div class="post">' + data + '</div>');
    });
}

function hideEditor() {
    var submit = $(".post_submit");
    $("#cke_editor").replaceWith($('.sample_editor'));
    submit.css("display", "none");
    $(".post_add").css("display", "inline");


    /*CKEDITOR.instances.editor.setData( '<p>Some other editor data.</p>', function()
    {
        CKEDITOR.instances.editor.resetDirty();
    } );*/
    CKEDITOR.instances.editor.destroy();

}
