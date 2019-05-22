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
    <script type="text/javascript">
        $(document).ready(function () {

            var pass1 = $("#pass1");
            $(".reg_field").keyup(function () {
                var th = $(this);
                if (th.val().length > 6 && th === pass1) th.next().removeClass("checkbox");
                else th.next().addClass("checkbox");
                if (th.val().length > 3 && th !== pass1) th.next().removeClass("checkbox");
                else th.next().addClass("checkbox");

                if (pass1.val() == $("#pass2").val()) $("#error_pass").text("");
                else {
                    $("#error_pass").text("Пароли не совпадают");
                }
            });
        });
    </script>
</head>
<body>

<div class="reg_container">
    <sf:form enctype="multipart/form-data" method="post" modelAttribute="user">
    <div class="reg_block">
        <label>
            Введите корпоративный ключ:
            <input name="orgKey" size="60" maxlength="60" class="reg_field"/>
            <img src="/resources/checkbox.jpg" class="checkbox">
        </label>
    </div>
    <div class="reg_block">
        <label>
            Введите Ваше имя:
            <sf:input path="name" size="50" maxlength="50" cssClass="reg_field"/>
            <img src="/resources/checkbox.jpg" class="checkbox">

        </label>
    </div>
    <div class="reg_block">
        <label>
            Введите логин:
            <sf:input path="login" size="20" maxlength="20" cssClass="reg_field"/>
            <img src="/resources/checkbox.jpg" class="checkbox">

        </label>
    </div>
    <div class="reg_block">
        <label>
            Введите пароль:
            <sf:password path="password" size="20" cssClass="reg_field" id="pass1"/>
            <img src="/resources/checkbox.jpg" class="checkbox">

        </label>
    </div>
    <div class="reg_block">
        <label>
            Повторите пароль:
            <input type="password" class="reg_field" id="pass2">
            <img src="/resources/checkbox.jpg" class="checkbox">
            <span class="jspError" id="error_pass"></span>
        </label>
    </div>
    <div class="reg_block">
        <label>
            Укажите ваш пол:
            <label for="male">М</label><input type="radio" class="reg_field" id="male">
            <label for="female">Ж</label><input type="radio" class="reg_field" id="female">
            <img src="/resources/checkbox.jpg" class="checkbox">
        </label>
    </div>
        <sf:errors path="name" cssClass="jspError"/>
        <sf:errors path="login" cssClass="jspError"/>
        <sf:errors path="password" cssClass="jspError"/>
        <span class="jspError" id="error_sex"></span>
    <div class="form_photo reg_block">
        <label>Загрузите фото:
            <input type="file" name="image" class="select_avatar">
        </label>
    </div>
        <div class="reg_block">
            <input class="commit" type="submit" value="Зарегистрировать">
        </div>
    </sf:form>
</div>
</body>
</html>
