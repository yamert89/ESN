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
    <title>Авторизация</title>
    <script type="text/javascript">
        $(document).ready(function () {
            if (getCookie('remember') === '1') $("#remember-me").prop('checked', true);
            $("#send").submit(function () {
                if ($("#remember-me").is(":checked")) document.cookie = 'remember=1; max-age=604800';
                else document.cookie = 'remember=0'
            });
        });

        function getCookie(name){
            console.log("cookies : " + document.cookie);
            var matches = document.cookie.match((new RegExp(name + "=[^;]*")));
            var res = matches != null ? matches.toString().replace(/.*=/, "") : null;
            console.log("get cookie : " + res);
            return res;
        }
    </script>
</head>
<body>
<div class="properties_board" id="auth">
    <form method="post" id="send" action="/login">
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Введите Ваше имя:</label>
                <input type="text" form="send" name="username"/>
            </div>

        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Введите пароль:</label>
                <input type="password" form="send" name="password">
            </div>

        </div>
        <div class="prop_line">
            <div class="inline">
                <label class="prop_label">Запомнить меня:</label>
                <input type="checkbox" name="remember-me" id="remember-me">
            </div>

        </div>
        <div class="prop_line">
            <div class="inline">
                <a href="/reg">Регистрация</a>
                <input type="submit" class="auth_submit_button commit" value="Войти">
            </div>

        </div>
        <div class="reg_block jspError">
            <c:if test="${error}">Неправильный логин или пароль</c:if>
            <c:if test="${reg}">Регистрация завершена</c:if>
        </div>

    </form>
</div>

</body>
</html>
