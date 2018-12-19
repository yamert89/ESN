<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<div class="reg_container">
    <sf:form method="post">
        <div class="reg_block">
            <label>
                Введите Ваше имя:&nbsp
                <sf:input type="text" path="login"/>
                <img src="${pageContext.request.contextPath}/resources/checkbox.jpg">
            </label>
        </div>
        <div class="reg_block">
            <label>
                Введите пароль:&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                <sf:password path="userPassword"/>
                <img src="${pageContext.request.contextPath}/resources/checkbox.jpg">
            </label>
        </div>
        <div class="reg_block">
            <a href="/user/reg">Регистрация</a>
            <input type="submit" class="auth_submit_button" value="Войти">
        </div>
        <div>${error}</div> //TODO
    </sf:form>
</div>

</body>
</html>
