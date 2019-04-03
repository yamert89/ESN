<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 29.11.2018
  Time: 10:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="<c:url value="/resources/static/center/chat/chat.css"/>">
    <script type="text/javascript">
        var blocker = false;
        $(document).ready(function () {
            $("#chat").addClass("selected");
            var textField = $(".new_genchat_message");
            var messBtn = $(".new_genchat_message_btn");
            messBtn.click(function () {
                var text = textField.val();
                if (text == "") return;
                var name = window.userName;
                var img = '/resources/avatars/' + $(".new_genchat_message").attr("data-img");
                var time = window.getDate(new Date());
                $(".message_container").prepend('<div class="message">\n' +
                    '        <div class="message_text">' + text + '</div>\n' +
                    '        <div class="message_info">\n' +
                    '            <img src="' + img + '" class="person_photo_small">\n' +
                    '            <div class="person_name">' + name + '</div>\n' +
                    '            <div class="message_time">' + time + '</div>\n' +
                    '        </div>\n' +
                    '    </div>');
                $.ajax({type:"POST", url:"/savemessage", data:{"userId":window.userId, "text":text, "time":time}});
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
                        renderMessage(el);
                    });
                    blocker = false;
                    console.log("false")
                }, 'json');
                blocker = true;
                console.log("true")
            })

        });

        function renderMessage(mes) {
            $(".message_container").append('<div class="message">\n' +
                '            <div class="message_text">' + mes.text + '</div>\n' +
                '            <div class="message_info">\n' +
                '                <img src="/resources/avatars/' + mes.imgUrl + '" class="person_photo_small">\n' +
                '                <div class="person_name">' + mes.userName + '</div>\n' +
                '                <div class="message_time">' + mes.time + '</div>\n' +
                '            </div>\n' +
                '        </div>')
        }


    </script>
</head>
<body>
<div class="chat_gen_container">
    <input type="text" placeholder="Добавить сообщение" class="new_genchat_message" data-img="${photo}">
    <button class="new_genchat_message_btn">Отправить</button>
    <div class="message_container">
    <c:forEach var="mes" items="${messages}">
        <div class="message">
            <div class="message_text">${mes.text}</div>
            <div class="message_info">
                <img src='<c:url value="/resources/avatars/${mes.imgUrl}"/>' class="person_photo_small">
                <div class="person_name">${mes.userName}</div>
                <div class="message_time"><fmt:formatDate value="${mes.time}" pattern="HH:mm:ss / dd.MM"/></div>
            </div>
        </div>
    </c:forEach>
    </div>
</div>
</body>
</html>
