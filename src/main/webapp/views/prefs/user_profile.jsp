<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 20.02.2019
  Time: 13:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Profile</title>
</head>
<body>
<div class="properties_board">
    <div class="prop_line inline_parent">
        <div class="inline"><c:set var="user" value='${sessionScope.get("user")}'/>
            <h3>${user.name}</h3>
        </div>
        <img class="user_photo inline" src="${user.photo}">
    </div>
    <div class="prop_line">
        <div class="prop_label">Должность:</div>
        <span>Инженер-таксатор</span>
    </div>
    <div class="prop_line">
        <div class="prop_label">Отдел:</div>
        <span>Отдел лесного планирования и проектирования</span>
    </div>
    <div class="prop_line">
        <div class="prop_label">Непосредственный начальник:</div>
        <span><a href="user_profile.html">Шубин Виктор Вячеславович</a></span>
    </div>
    <div class="prop_line">
        <div class="prop_label">E-mail:</div>
        <span><a href="mailto:shurikporohin@rambler.ru">shurikporohin@rambler.ru</a></span>
    </div>
    <div class="prop_line">
        <div class="prop_label">Мобильный телефон:</div>
        <span>+7(960)0174990</span>
    </div>
    <div class="prop_line">
        <div class="prop_label">Рабочий телефон:</div>
        <span>56-68-92</span>
    </div>
    <div class="prop_line">
        <div class="prop_label">Внутренний телефон:</div>
        <span>128</span>
    </div>

    <div class="prop_line">
        <input class="commit" type="submit" value="Применить Настройки">
    </div>
    <div class="prop_line">
        <input class="commit" type="submit" value="Удалить аккаунт">
    </div>

</div>
</body>
</html>
