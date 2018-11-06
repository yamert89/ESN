<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: Пендальф Синий
  Date: 01.11.2018
  Time: 20:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Auth</title>
</head>
<body>
<sf:form method="post" modelAttribute="">
    <label class="reg_field">
        Введите Ваше имя
        <sf:input path="userName" type="text" name="name"/>
        <img src="${pageContext.request.contextPath}/resources/checkbox.jpg">
    </label><br>
    <label class="reg_field">
        Введите пароль&nbsp&nbsp&nbsp&nbsp&nbsp
        <sf:input path="userPassword" type="password" name="password"/>
        <img src="${pageContext.request.contextPath}/resources/checkbox.jpg">
    </label><br>
</sf:form>
</body>
</html>
