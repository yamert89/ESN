var submit;
$(document).ready(function () {
    $('.post_add').click(function () {
        showEditor();
    });
    submit = $(".post_submit");
    submit.click(function () {
        var data;
        try{
            data = CKEDITOR.instances.editor.getData();
        }catch (e) {
            alert("error");
            return;
        }

        hideEditor();

        var NAME = ''; //TODO server
        var IMG = ''; //TODO server
        var TIME = window.getCurrentDate();

        $(".posts").prepend('<div class="post">' +
            '<div class="message_info_wrapper">' +
            '<div class="message_info_w">' +
            '<img src="' + IMG + '" class="person_photo_small">' +
            '<div class="person_name_w">' + NAME + '</div>' +
            '<div class="message_time_w">' + TIME + '</div>' +
            '</div>' +
            '</div>' + data + '</div');

        //TODO server save
    });
});

function showEditor() {

    try {
        var editor = CKEDITOR.replace('editor');
        /*editor.config.autoParagraph = false;*/
        CKEDITOR.instances.editor.setData('<p></p>');
    } catch (e) {
        /*CKEDITOR.instances.editor.updateElement();
        CKEDITOR.replace('editor');*/
    }


    $(".post_add").css("display", "none");
    submit.css({"display": "inline"});

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

/*<div class="message_info_wrapper">
    <div class="message_info_w">
    <img src="" class="person_photo_small">
    <div class="person_name_w">Булыгина Мария Ивановна</div>
<div class="message_time_w">10: 30 21.09.2018</div>
</div>
</div>*/

