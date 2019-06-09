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
    <form method="post" id="send" action="/login">
        <div class="reg_block">
            <label>
                Введите Ваше имя:&nbsp
                <input type="text" form="send" name="username"/>

            </label>
        </div>
        <div class="reg_block">
            <label>
                Введите пароль:&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                <input type="password" form="send" name="password">

            </label>
        </div>
        <div class="reg_block">
            <label>
                Запомнить меня:&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                <input type="checkbox" name="remember-me" id="remember-me">
            </label>
        </div>
        <div class="reg_block">
            <a href="/reg">Регистрация</a>
            <input type="submit" class="auth_submit_button" value="Войти">
        </div>
        <div class="reg_block jspError"><c:if test="${error}">Неправильный логин или пароль</c:if></div>

    </form>
</div>

</body>
</html>
