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
    <script type="text/javascript" src='<c:url value="/resources/static/center/chat/gen_chat.js"/>'></script>
</head>
<body>
<c:set var="orgUrl" value="${sessionScope.get('org').getUrlName()}"/>
<div class="chat_gen_container">
    <input type="text" placeholder="Добавить сообщение" class="new_genchat_message" data-img="${photo}">
    <button class="new_genchat_message_btn">Отправить</button>
    <div class="message_container">
    <c:forEach var="mes" items="${messages}">
        <div class="message">
            <div class="message_text">${mes.text}</div>
            <div class="message_info">
                <img src='<c:url value="/resources/data${mes.imgUrl}"/>' class="person_photo_small">
                <div class="person_name">${mes.userName}</div>
                <div class="message_time"><fmt:formatDate value="${mes.time.getTime()}" pattern="HH:mm:ss / dd.MM"/></div>
            </div>
        </div>
    </c:forEach>
    </div>
</div>
</body>
</html>
