<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <link rel="stylesheet" href="<core:url value="/resources/static/center/chat/gen_chat.css"/>">
    <script type="text/javascript">
        $(document).ready(function () {
            setCurrentDate($(".message_time"));
        });
    </script>
</head>
<body>
<div class="chat_gen_container">
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
