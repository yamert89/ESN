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
    <link rel="stylesheet" href="<c:url value="/resources/static/center/groups/groups.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/center/staff/flowchart/block.css"/>">
    <script src="<c:url value="/resources/static/center/groups/groups.js"/>"></script>
</head>
<body>
<div class="groups_container">
    <div class="staff_container" id="all_staff_cont">
<c:forEach var="person" items="${employers}">
    <div class="person_staff">
        <img src='<c:url value="/resources/data${person.photo}"/>' class="person_photo_staff">
        <div class="person_point" data-p-id="${person.id}" data-p-login="${person.login}">
            <div class="person_name_staff">${person.shortName}</div>
            <div class="person_position">${person.position}</div>
        </div>
    </div>
</c:forEach>
    </div>

<div class="central_wrapper">
    <img src='<c:url value="/resources/data/app/next.png"/>' class="arrow" id="right_arrow">
    <img src='<c:url value="/resources/data/app/next.png"/>' class="arrow" id="left_arrow">

    <div class="groups">
        <c:forEach var="group" items="${groupsNames}">
            <div class="group">${group}</div>
        </c:forEach>
    </div>
    <button class="group_btn" id="add_group">Добавить группу</button>
    <button class="group_btn" id="save_group">Сохранить группу</button>
    <button class="group_btn" id="del_group">Удалить группу</button>

</div>
<div class="staff_container" id="group_staff_cont">


</div>

</div>
<div class="shadow">
    <c:forEach var="group" items="${groups}">
        <div class="group_temp" data-name="${group.key}">
            <c:forEach var="person" items="${group.value}">
                <div class="person_staff">
                    <img src='<c:url value="/resources/data${person.photo}"/>' class="person_photo_staff">
                    <div class="person_point" data-p-id="${person.id}" data-p-login="${person.login}">
                        <div class="person_name_staff">${person.shortName}</div>
                        <div class="person_position">${person.position}</div>
                    </div>
                </div>
            </c:forEach>

        </div>
    </c:forEach>
</div>

</body>
</html>
