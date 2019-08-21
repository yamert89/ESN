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
    <title>Регистрация</title>
    <script type="text/javascript">
        $(document).ready(function () {


            $(".reg_field").keyup(function () {
                var pass1 = $("#pass1");
                var pass2 = $("#pass2");
                var regfield = $(this);
                var constraint = regfield[0] == $("#small_input")[0] ? 59 :
                    regfield[0] == pass1[0] ? 6 : 3;

                checkboxSateChange(regfield, constraint);

                if (pass1.val() === pass2.val()) $("#error_pass").text("");
                else {
                    $("#error_pass").text("Пароли не совпадают");
                }

            });

            $("form").submit(function () {
                var res = '';
                try{
                    $("#name #firstName #thirdName").each(function (idx, el) {
                        res = res + '_' + el.getAttribute('value');
                    });

                    $("#input").val(res.substr(1));
                }catch (e) {
                    console.log(e);
                }
            })
        });

        function checkboxSateChange(regfield, constrains) {
            if (regfield.val().length > constrains) regfield.next().next().removeClass("off");
            else regfield.next().next().addClass("off");
        }
    </script>
</head>
<body>

<div class="properties_board" id="reg">
    <sf:form enctype="multipart/form-data" method="post" modelAttribute="user">
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label fixed_max">Введите корпоративный ключ:</label>
                <input name="orgKey" size="60" maxlength="60" class="reg_field fixed_min" id="small_input"/>
                <span class="star">*</span>
                <img src="/resources/data/app/checkbox.jpg" class="checkbox off">
            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Фамилия:</label>
                <sf:input path="name" size="20" maxlength="20" cssClass="reg_field"/>
                <span class="star">*</span>
                <img src="/resources/data/app/checkbox.jpg" class="checkbox off">
            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Имя:</label>
                <input id="firstName" size="20" maxlength="20" class="reg_field"/>
                <span class="star">*</span>
                <img src="/resources/data/app/checkbox.jpg" class="checkbox off">
            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Отчество:</label>
                <input id="thirdName" size="20" maxlength="20" class="reg_field"/>
                <span class="star">*</span>
                <img src="/resources/data/app/checkbox.jpg" class="checkbox off">
            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Введите логин:</label>
                <sf:input path="login" size="20" maxlength="20" cssClass="reg_field"/>
                <span class="star">*</span>
                <img src="/resources/data/app/checkbox.jpg" class="checkbox off">

            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Введите пароль:</label>
                <sf:password path="password" size="20" cssClass="reg_field" id="pass1"/>
                <span class="star">*</span>
                <img src="/resources/data/app/checkbox.jpg" class="checkbox off">

            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Повторите пароль:</label>
                <input type="password" class="reg_field" id="pass2">
                <span class="star">*</span>
                <img src="/resources/data/app/checkbox.jpg" class="checkbox off">
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
                <span class="star">*</span>
                <img src="/resources/data/app/checkbox.jpg" class="checkbox off">
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
