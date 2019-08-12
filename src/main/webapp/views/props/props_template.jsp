<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 20.02.2019
  Time: 13:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href='<c:url value="/resources/static/props/props.css"/>'>
    <link rel="stylesheet" href='<c:url value="/resources/static/gen.css"/>'>
    <script type="text/javascript" src='<c:url value="/resources/libs/jquery-3.4.1.min.js"/>'></script>
    <script src="<c:url value="/resources/static/progress.js"/>"></script>
</head>
<body>
<t:insertAttribute name="pref_page"/>
<div class="progress_wr_hidden">
    <img src="/resources/data/app/progress.gif" class="progress_hidden">
</div>
</body>
</html>
