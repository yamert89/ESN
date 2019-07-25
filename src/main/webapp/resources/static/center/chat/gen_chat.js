var blocker = false;
$(document).ready(function () {
    var chat = $("#chat");
    chat.addClass("selected");
    chat.find("img").css("display", "none");
    var textField = $(".new_genchat_message");
    var messBtn = $(".new_genchat_message_btn");
    messBtn.click(function () {
        var mes = {};
        mes.text = textField.val();
        if (mes.text == "") return;
        mes.userName = window.userName;
        mes.imgUrl = $(".new_genchat_message").attr("data-img");
        mes.time = window.getDate(new Date());
        renderMessage(mes, 'my');
        $.ajax({type:"POST", url:"/savemessage", data:{"userId":window.userId, "text":mes.text, "time":mes.time}});
        textField.val('');
    });
    $(document).keypress(function (event) {
        if (event.which == 13) messBtn.click();
    });

    $(".message_container").scroll(function () {
        if (blocker || $(this)[0].scrollHeight - ($(this)[0].scrollTop + $(this).height()) > 300) return;
        $.get("/chatpiece", {}, function (data) {
            if (data == null) return;
            data.forEach(function (el) {
                renderMessage(el, 'piece');
            });
            blocker = false;
            console.log("false")
        }, 'json');
        blocker = true;
        console.log("true")
    });

    $(document).on('click', '.delete_message', function () {
        $(this).parent().parent().remove();
        var text = $(this).parent().prev().text();
        $.ajax({url : "/deletemessage", method : "POST", data :{text: text}})
    })

});

/*type :
* - 'my' - my new message
* - 'piece' - message loaded after scrolling
* - 'mailing' - message loaded after ws mailing*/
function renderMessage(mes, type) {
    var visibleCross = type !== 'my' ? 'style=""' :  ' style="display: inline-block";';
    var data = '<div class="message">\n' +
        '            <div class="message_text">' + mes.text + '</div>\n' +
        '            <div class="message_info">\n' +
        '                <img src="/resources/data' + mes.imgUrl + '" class="person_photo_small">\n' +
        '                <div class="person_name">' + mes.userName + '</div>\n' +
        '                <div class="message_time">' + mes.time + '</div>\n' +
        '                <img class="delete_message" src="/resources/cross.png"' + visibleCross + '>' +
        '        </div>';
    if(type === "piece") $(".message_container").append(data);
    else $(".message_container").prepend(data);

}
