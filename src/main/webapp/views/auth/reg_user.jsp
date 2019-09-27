<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <script type="text/javascript" src="<c:url value='/resources/static/resizer.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/libs/resize.js-master/resize.js-master/resize.js'/>"></script>
    <script type="text/javascript">
        $(document).ready(function () {

            $(".reg_field").change(checkboksListener);
            $(".reg_field").keyup(checkboksListener);
            $("form").submit(function (e) {



                var res = '';
                var fstName = $("#firstName");
                var name = $("#name");
                var trdName = $("#thirdName");
                try{
                    res = name.val() + '_' + fstName.val() + '_' + trdName.val();
                    name.val(res);
                }catch (e) {
                    console.log(e);
                }
                if (fstName.val() == "") {
                    e.preventDefault();
                   $("#error_sex").text("Введите имя");
                   setTimeout(stopProgress, 500);
                }

                if ($("#pass1").val() !== $("#pass2").val()) {
                    e.preventDefault();
                    $("#error_pass").text("Пароли не совпадают");
                    return;
                }
               /* var data = new FormData();*/
                var files = $(".select_avatar").get(0).files;
                if (files.length > 0){
                    e.preventDefault();
                    resizePhoto(files[0], 128, 128);
                }


                //files[0] = window.photo;
                console.log("dfs");
                /*data.append( 'file', file);
                data.append('shared', shared);*/



            })



        });

        function submitFormManually(photo) {
            //$(".select_avatar").get(0).files[0] = photo.file;

            var fData = new FormData();
            fData.append("orgKey", $("[name=orgKey]").val());
            fData.append("name", $("[name=name]").val());
            fData.append("login", $("[name=login]").val());
            fData.append("password", $("[name=password]").val());
            fData.append("sex", $("[name=sex]").val());
            fData.append("image", photo.file);
            var x = new XMLHttpRequest();
            x.open("POST", "/reg", false);
            x.onload = function () {
                document.open();
                document.write(x.responseText);
                document.close();
            };
            x.send(fData);




            /*$.ajax({url : '/reg', method : "post", contentType:false, processData: false, data : fData, success : function () {
                    location.href = '/auth?reg=success';
                }});*/


        }

        function checkboksListener(){
            var pass1 = $("#pass1");
            var pass2 = $("#pass2");
            var regfield = $(this);
            var firstName = $("#firstName");
            var name = $("#name");
            var lg = $("#login");

            var constraint = regfield[0] === $(".small_input")[0] ? 59 :
                regfield[0] === pass1[0] ? 6 :
                    regfield[0] === name[0] || regfield[0] === firstName[0] ? 1 :
                        regfield[0] === lg[0] ? 3 : 0;

            if (pass1.val() === pass2.val()) {
                $("#error_pass").text("");
                constraint = 0;
                checkboxSateChange(regfield, constraint);
            } else $("#error_pass").text("Пароли не совпадают");
            if (constraint == 0 && regfield[0] == pass2[0]) return;

           checkboxSateChange(regfield, constraint);

        }


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
                <input name="orgKey" size="60" maxlength="60" class="reg_field fixed_min small_input"/>
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
                <span class="star" style="opacity: 0">*</span>
                <img src="/resources/data/app/checkbox.jpg" class="checkbox off"  style="opacity: 0">
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
                <input type="radio" name="sex" value="m" checked class="small_input"/>
                <label class="prop_label fixed_min">Женский:</label>
                <input type="radio" name="sex" value="f" class="small_input"/>
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
