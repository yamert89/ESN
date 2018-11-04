<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: Пендальф Синий
  Date: 02.11.2018
  Time: 21:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration</title>
</head>
<body>
<div class="reg_container">
    <sf:form enctype="multipart/form-data" method="post" modelAttribute="user">
    <div class="reg_block">
        <label class="reg_field">
            Введите Ваше имя:
            <sf:input path="name" type="text"/>
            <img src="../../resources/checkbox.jpg">
            <sf:errors path="name" cssClass="error"/>
        </label>
    </div>
    <div class="reg_block">
        <label class="reg_field">
            Введите пароль:
            <sf:input path="password" type="password"/>
            <img src="../../resources/checkbox.jpg">
            <sf:errors path="password" cssClass="error"/>
        </label>
    </div>
    <div class="reg_block">
        <label class="reg_field">
            Повторите пароль:
            <input type="password">
            <img src="../../resources/checkbox.jpg">
        </label>
    </div>
    <div class="form_photo reg_block">
        <label class="reg_field">Загрузите фото:
            <input type="file" name="avatar" accept="image/*" class="select_avatar">
            <img src="../../resources/checkbox.jpg">
        </label>
    </div>
    </sf:form>
</div>
</body>
</html>
