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
                var mes = {};
                mes.text = textField.val();
                if (mes.text == "") return;
                mes.userName = window.userName;
                mes.imgUrl = $(".new_genchat_message").attr("data-img");
                mes.time = window.getDate(new Date());
                renderMessage(mes, false);
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
                        renderMessage(el, true);
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

        function renderMessage(mes, after) {
            var data = '<div class="message">\n' +
                '            <div class="message_text">' + mes.text + '</div>\n' +
                '            <div class="message_info">\n' +
                '                <img src="/resources/avatars/' + mes.imgUrl + '" class="person_photo_small">\n' +
                '                <div class="person_name">' + mes.userName + '</div>\n' +
                '                <div class="message_time">' + mes.time + '</div>\n' +
                '                <img class="delete_message" src="/resources/cross.png" style="display: inline-block">  ' +
                '        </div>';
            if(after) $(".message_container").append(data);
            else $(".message_container").prepend(data);

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
