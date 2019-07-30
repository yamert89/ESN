<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href='<c:url value="/resources/static/index.css"/>'>
    <link rel="stylesheet" href='<c:url value="/resources/static/gen.css"/>'>
    <link rel="stylesheet" href='<c:url value="/resources/static/tools/tools.css"/>'>
    <link rel="stylesheet" href='<c:url value="/resources/static/center/notes/notes.css"/>'>
    <link rel="stylesheet" href='<c:url value="/resources/static/contacts/contacts.css"/>'>
    <link rel="stylesheet" href='<c:url value="/resources/libs/air-datepicker-master/dist/css/datepicker.css"/>'>
    <script src='<c:url value="/resources/libs/jquery-3.4.1.min.js"/>'></script>
    <script src='<c:url value="/resources/libs/air-datepicker-master/dist/js/datepicker.js"/>'></script>
    <script src='<c:url value="/resources/libs/sock.js"/>'></script>
    <script src='<c:url value="/resources/libs/stomp.js"/>'></script>
    <script src="<c:url value="/resources/libs/ckeditor/ckeditor.js"/>"></script>
    <script type="text/javascript" src='<c:url value="/resources/static/index.js"/>'></script>
    <script type="text/javascript" src='<c:url value="/resources/static/contacts/contacts.js"/>'></script>
    <title>CHAT</title>
</head>
<header style='background-image: url(${"/resources/data".concat(sessionScope.get("org").getHeaderPath())}); background-size: 100% 100%;'>
    <c:set var="user" value='${sessionScope.get("user")}'/>
    <c:set var="orgUrl" value='${sessionScope.get("org").getUrlName()}'/>
    <div class="title" align="center" data-url='${orgUrl}' data-org-id='${sessionScope.get("org").getId()}'>${user.organization.name}</div>
    <div class="user">
        <span class="user_name" data-login="${user.login}" data-user_id="${user.id}">${user.name}</span>
        <div class="wr">
            <img src='<c:url value="/resources/data${user.photo}"/>' class="user_photo">
            <a href="/logout" class="logout">выход</a>
        </div>


    </div>
</header>
<body>
<div class="container">
    <div class="tools">
        <div class="tool" id="wall" onclick="location.href = '/${orgUrl}/wall'">Лента
            <img src='<c:url value="/resources/new_message.png"/>' class="new_gen_mes" id="wall_m"></div>
        <div class="tool" id="chat" onclick="location.href = '/${orgUrl}/chat'">Чат
            <img src='<c:url value="/resources/new_message.png"/>' class="new_gen_mes" id="chat_m"></div>
        <div class="tool" id="staff" onclick="location.href = '/${orgUrl}/staff'">Структура</div>
        <div class="tool" id="groups" onclick="location.href = '/${orgUrl}/groups'">Группы</div>
        <div class="tool" id="storage" onclick="location.href = '/${orgUrl}/storage'">Файлообменник</div>
        <div class="tool" id="notes" onclick="location.href = '/${orgUrl}/notes'">Заметки</div>
        <div class="tool" id="calendar" onclick="location.href = '/${orgUrl}/calendar'">Календарь</div>
        <%--<div class="tool" id="apps" onclick="location.href = '/${orgUrl}/apps'">Приложения</div>--%>
        <div class="datepicker-here"></div>
        <div class="log log_message">Это просто такой текст лога</div>
    </div>
    <div class="center">
        <t:insertAttribute name="center"/>
    </div>
    <div class="contacts">
        <div class="contacts-frame"></div>
    </div>

</div>
</body>
</html>
