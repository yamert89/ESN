<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <link rel="stylesheet" href="<c:url value="/resources/static/center/chat/gen_chat.css"/>">
    <script type="text/javascript">
        $(document).ready(function () {
            var textField = $(".new_genchat_message");
            var messBtn = $(".new_genchat_message_btn");
            messBtn.click(function () {
                var text = textField.val();
                if (text == "") return;
                var name = window.userName;
                var img = window.avatar;
                var time = window.getCurrentDate();
                messBtn.after('<div class="message">\n' +
                    '        <div class="message_text">' + text + '</div>\n' +
                    '        <div class="message_info">\n' +
                    '            <img src="' + img + '" class="person_photo_small">\n' +
                    '            <div class="person_name">' + name + '</div>\n' +
                    '            <div class="message_time">' + time + '</div>\n' +
                    '        </div>\n' +
                    '    </div>');
                $.ajax({type:"POST", url:"/savemessage", data:{"userId":window.userId, "text":text, "time":time, "orgUrl":window.orgUrl}})
            })
        });


    </script>
</head>
<body>
<div class="chat_gen_container">
    <input type="text" placeholder="Добавить сообщение" class="new_genchat_message" data-img="${photo}">
    <button class="new_genchat_message_btn">Отправить</button>
    <c:forEach var="mes" items="${messages}">
        <div class="message">
            <div class="message_text">${mes.text}</div>
            <div class="message_info">
                <img src='<c:url value="${mes.imgUrl}"/>' class="person_photo_small">
                <div class="person_name">${mes.userName}</div>
                <div class="message_time">${mes.time}</div>
            </div>
        </div>
    </c:forEach>
    <div class="message">
        <div class="message_text">Сообщение 1</div>
        <div class="message_info">
            <img src="" class="person_photo_small">
            <div class="person_name">Маша Петровна Васильева</div>
            <div class="message_time">10: 30 21.09.2018</div>
        </div>
    </div>
    <div class="message">
        <div class="message_text">Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1
            Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1
            Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1
            Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1
            Сообщение 1 Сообщение 1 Сообщение 1 Сообщение 1</div>
        <div class="message_info">
            <img src="" class="person_photo_small">
            <div class="person_name">Маша</div>
            <div class="message_time">10: 30 21.09.2018</div>
        </div>

    </div>
</div>
</body>
</html>