
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 29.11.2018
  Time: 10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Стена</title>
    <link rel="stylesheet" href="<c:url value="/resources/static/center/wall/wall.css"/>">
    <script type="text/javascript" src="<c:url value="/resources/static/center/wall/wall.js"/>"></script>
    <script type="text/javascript"  src='<c:url value="/resources/static/center/wall/wall.js"/>'></script>
</head>
<body>
<%--<img src="/resources/6.gif">--%>
<c:set var="user" value="${sessionScope.get('user')}"/>
<c:set var="orgUrl" value="${sessionScope.get('org').getUrlName()}"/>
<div class="wall_container">
<div class="post_add_wrapper">
    <button class="post_add" data-name="${user.name}" data-img="${user.photo}"
            data-userId="${user.id}" data-ogrUrl="${orgUrl}"  data-img-small="${user.photo_small}">Добавить новость</button></div>
<textarea name="editor" class="editor" rows="10" cols="80"></textarea>
<button class="post_submit">Опубликовать новость</button>
<textarea name="sample_editor" class="sample_editor" rows="10" cols="80"></textarea>
<div class="posts">
    <c:forEach var="mes" items="${messages}">
        <div class="post">
            <div class="message_info_wrapper">
                <div class="message_info_w">
                    <img src='<c:url value="/resources/data${mes.imgUrl}"/>' class="person_photo_small">
                    <div class="person_name_w">${mes.userName}</div>
                    <div class="message_time_w"><fmt:formatDate value="${mes.time.getTime()}" pattern="HH:mm:ss / dd.MM"/></div>
                </div>
            </div>
            ${mes.text}
        </div>
    </c:forEach>

</div>
</div>
</body>
</html>
