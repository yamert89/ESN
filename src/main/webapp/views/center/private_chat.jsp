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
    <link rel="stylesheet" href="<c:url value="/resources/static/index.css"/>">
    <script type="text/javascript">
        $(document).ready(function () {
            var textField = $(".new_genchat_message");
            var messBtn = $(".new_genchat_message_btn");
            messBtn.click(function () {
                var text = textField.val();
                if (text == "") return;
                var comp_id = $(".person_container").attr('data-companion-id');
                var time = window.getCurrentDate();
                $(".private_chat_container").prepend('<div class="private_chat comment_bubble_right"><div class="time-right">' + time + '</div>' + text + '</div>');
                $.ajax({type:"POST", url:"/save_private_message/" + comp_id, data:{"text":text}});
                textField.val('');
            });
            $(document).keypress(function (event) {
                if (event.which == 13) messBtn.click();
            })
        });
    </script>
</head>
<body>
<div class="private_chat_profile">
    <div class="person_container" data-companion-id="${companion.id}">
        <img src='<c:url value="/resources/avatars/${companion.photo}"/>' class="person_photo_chat"><br>
        <c:if test="${companion.netStatus} == false">
            <div class="net_status_circle" id="net_status_off"></div>
            <span class="net_status">не в сети</span>
        </c:if>
        <c:if test="${companion.netStatus} == true">
            <div class="net_status_circle"></div>
            <span class="net_status">в сети</span>
        </c:if>
    </div>
    <span class="person_name_chat">${companion.name}</span><br>
    <div class="new_message_container">
        <input type="text" placeholder="Добавить сообщение" class="new_genchat_message flex">
        <button class="new_genchat_message_btn flex">Отправить</button>
    </div>
</div>
<div class="private_chat_container">

    <c:forEach var="mes" items="${messages}">
        <c:if test="${mes.value == true}">
            <div class="private_chat comment_bubble_right"><div class="time-right">
        </c:if>
        <c:if test="${mes.value == false}">
            <div class="private_chat comment_bubble_left"><div class="time-left">
        </c:if><fmt:formatDate value="${mes.key.time.time}" pattern="HH:mm:ss   dd.MM.yyyy"/>
        </div>${mes.key.text}
        </div>
    </c:forEach>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_left">Сообщение</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2</div>
    <div class="private_chat comment_bubble_right">Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2
        Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2
        Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2
        Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2
        Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2
        Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2
        Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2
        Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2
        Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2
        Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 Сообщение2 </div>
</div>
</body>
</html>
