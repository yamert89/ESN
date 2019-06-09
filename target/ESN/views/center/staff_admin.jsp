<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Title</title>
    <link rel="stylesheet" href="<c:url value="/resources/static/center/staff/flowchart/block.css"/>">
    <script type="text/javascript" src="<c:url value="/resources/static/center/staff/staff.js"/>"></script>
</head>
<body>
<div class="tree_wrapper">

    <div class="flowchart_wrapper">
        <iframe src='<c:url value="/resources/static/center/staff/flowchart/flowchart.html"/>' class="flowchart_frame" id="iframe1"></iframe>
        <div class="features">
        <button class="save_flowchart" disabled>Сохранить структуру</button>
        <button class="clear_flowchart">Очистить структуру</button>
        <button class="staff_choose_btn">Редактировать отдел</button>
        <button class="staff_add_btn" disabled>Сохранить отдел</button>
        </div>
    </div>

    <div class="staff_container">

    </div>
</div>
</body>
</html>