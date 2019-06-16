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

<div class="properties_board" id="reg">
    <sf:form enctype="multipart/form-data" method="post" modelAttribute="user">
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Введите корпоративный ключ:</label>
                <input name="orgKey" size="60" maxlength="60" class="reg_field" id="small_input"/>
                <img src="/resources/checkbox.jpg" class="checkbox">
            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Введите Ваше имя:</label>
                <sf:input path="name" size="50" maxlength="50" cssClass="reg_field"/>
                <img src="/resources/checkbox.jpg" class="checkbox">
            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Введите логин:</label>
                <sf:input path="login" size="20" maxlength="20" cssClass="reg_field"/>
                <img src="/resources/checkbox.jpg" class="checkbox">

            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Введите пароль:</label>
                <sf:password path="password" size="20" cssClass="reg_field" id="pass1"/>
                <img src="/resources/checkbox.jpg" class="checkbox">

            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Повторите пароль:</label>
                <input type="password" class="reg_field" id="pass2">
                <img src="/resources/checkbox.jpg" class="checkbox">
            </div>
            <span class="jspError" id="error_pass"></span>
        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label fixed_max">Укажите ваш пол:</label>
                <label class="prop_label fixed_min">Мужской:</label>
                <sf:radiobutton path="male" value="true" id="small_input"/>
                <label class="prop_label fixed_min">Женский:</label>
                <sf:radiobutton path="male" value="false" id="small_input"/>
                <img src="/resources/checkbox.jpg" class="checkbox">
            </div>
        </div>
        <sf:errors path="name" cssClass="jspError"/>
        <sf:errors path="login" cssClass="jspError"/>
        <sf:errors path="password" cssClass="jspError"/>
        <span class="jspError" id="error_sex"></span>

        <div class="prop_line form_photo">
            <div class="inline">
                <label class="prop_label">Загрузите фото:</label>
                <input type="file" name="image" class="select_avatar">

            </div>
        </div>
        <div class="prop_line">
            <input class="commit" type="submit" value="Зарегистрировать">
        </div>
    </sf:form>
</div>
</body>
</html>
