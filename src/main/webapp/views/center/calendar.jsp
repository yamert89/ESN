<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 29.11.2018
  Time: 10:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="<c:url value="/resources/static/center/calendar/frame_style.css"/>">
</head>
<body>
<iframe src='<c:url value="/resources/static/center/calendar/calendar.html"/>' class="calendar_frame" frameborder="0"></iframe>
</body>
</html>
