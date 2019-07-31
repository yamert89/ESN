var submit;
var blocker = false;
var wall = $("#wall");
wall.addClass("selected");
wall.find("img").css("display", "none");


$(document).ready(function () {
    var addButton = $('.post_add').first();
    window.userId = addButton.attr("data-userId");
    window.orgUrl = addButton.attr("data-ogrUrl");
    window.avatar = addButton.attr("data-img");
    window.avatar_small = addButton.attr("data-img-small");
    window.userName = addButton.attr("data-name");
    addButton.click(function () {
        showEditor();
    });
    submit = $(".post_submit");
    submit.click(function () {
        var data;
        try{
            data = CKEDITOR.instances.editor.getData();
        }catch (e) {
            return;
        }

        hideEditor();
        var mes = {};
        mes.userName = window.userName;
        mes.imgUrl = window.avatar_small;
        mes.time = window.getDate(new Date());
        mes.text = data;
        if (mes.text == "") return;

        var sizeMes = 500;
        if (mes.text.length > sizeMes) {
            notify('Максимальный размер сообщения ' + sizeMes +' символов.');
            return;
        }

        renderPost(mes, 'my');

        /*$(".posts").prepend('<div class="post">' +
            '<div class="message_info_wrapper">' +
            '<div class="message_info_w">' +
            '<img src="' + img + '" class="person_photo_small">' +
            '<div class="person_name_w">' + name + '</div>' +
            '<div class="message_time_w">' + time + '</div>' +
            '</div>' +
            '</div>' + data + '</div>');*/

        $.ajax({type:"POST", url:"/savepost", data:{"userId":userId, "text":data, "time":mes.time, "orgUrl":orgUrl}})


    });

    $(".posts").scroll(function () {
        if (blocker || $(this)[0].scrollHeight - ($(this)[0].scrollTop + $(this).height()) > 300) return;

        $.get("/wallpiece", function (data) {
            if (Object.keys(data).length === 0) return;
            data.forEach(function (el) {
                renderPost(el, 'piece');
            });
            blocker = false;
            console.log("false")
        }, 'json');
        blocker = true;
        console.log("true")
    });

    $(document).on('click', '.delete_message', function () {
        $(this).parent().parent().remove();
        var text = $(this).parent().parent().children().last().text();
        $.ajax({url : "/deletepost", method : "POST", data :{text: text}})
    })


});



function renderPost(mes, type) {
    var visibleCross = type !== 'my' ? 'style=""' :  ' style="display: inline-block";';
    var data = '<div class="post">\n' +
        '            <div class="message_info_wrapper">\n' +
        '                <div class="message_info_w">\n' +
        '                    <img src="/resources/data' + mes.imgUrl + '" class="person_photo_small">\n' +
        '                    <div class="person_name_w">' + mes.userName + '</div>\n' +
        '                    <div class="message_time_w">' + mes.time + '</div>\n' +
        '                </div><img class="delete_message" src="/resources/cross.png"' + visibleCross + '>' +
        '            </div>\n' + mes.text + '</div>';
    if(type === "piece") $(".posts").append(data);
    else $(".posts").prepend(data);

}

function showEditor() {
    try {
        CKEDITOR.replace('editor');
        /*editor.config.autoParagraph = false;*/
        CKEDITOR.instances.editor.setData('<p></p>');
    } catch (e) {
        alert("Ошибка выполнения скрипта");
    }

    $(".post_add").css("display", "none");
    submit.css({"display": "inline"});

}

function hideEditor() {
    $("#cke_editor").replaceWith($('.sample_editor'));
    submit.css("display", "none");
    $(".post_add").css("display", "inline");
    CKEDITOR.instances.editor.destroy();

}

/*<div class="message_info_wrapper">
    <div class="message_info_w">
    <img src="" class="person_photo_small">
    <div class="person_name_w">Булыгина Мария Ивановна</div>
<div class="message_time_w">10: 30 21.09.2018</div>
</div>
</div>*/
