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
    <title>Title</title>
    <link rel="stylesheet" href="<core:url value="/resources/static/auth/start.css"/>">
    <link rel="stylesheet" href="<core:url value="/resources/static/gen.css"/>">
</head>
<body>
<t:insertAttribute name="auth_reg"/>
</body>
</html>
