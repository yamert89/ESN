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
    <link rel="stylesheet" href="<c:url value="/resources/static/center/chat/private_chat.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/index.css"/>">
</head>
<body>
<div class="private_chat_profile">
    <div class="person_container">
        <img src="" class="person_photo_chat"><br>
        <div class="net_status_circle" style=""></div>
        <span class="net_status">В сети</span>
    </div>
    <span class="person_name_chat">Иванов Иван Иванович</span><br>
</div>
<div class="private_chat_container">
    <c:forEach var="mes" items="${messages}">
        <c:if test="messages.value == true">
            <div class="private_chat comment_bubble_right">${mes.key}</div>
        </c:if>
        <c:if test="messages.value == false">
            <div class="private_chat comment_bubble_left">${mes.key}</div>
        </c:if>
    </c:forEach>
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
