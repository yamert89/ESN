<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Пендальф Синий
  Date: 04.11.2018
  Time: 11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="<core:url value="/resources/static/gen.css"/>">
    <link rel="stylesheet" href="<core:url value="/resources/static/props/props.css"/>">
    <script src="<core:url value="/resources/libs/jquery-3.4.1.min.js"/>"></script>
    <script src="<core:url value="/resources/static/progress.js"/>"></script>
</head>
<body>
<t:insertAttribute name="auth_reg"/>
<div class="progress_wr_hidden">
    <img src="/resources/data/app/progress.gif" class="progress_hidden">
</div>
</body>
</html>
