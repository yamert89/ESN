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
    <script type="text/javascript">
        $(document).ready(function () {
            var board = $(".properties_board");
            var orgUrl = board.attr("data-orgurl");

            $("#main-btn").click(function () {
                location.href = "/" + orgUrl + "/wall/"
            });
            $("span").each(function (i, el) {
                if (el.innerHTML === '' || el.firstChild.innerHTML === '') el.innerHTML = 'Не указан';

            })

        })
    </script>
</head>
<body>
<div class="properties_board" data-orgurl="${sessionScope.get("orgUrl")}">
    <div class="prop_line inline_parent">
        <div class="inline"><c:set var="user" value='${sessionScope.get("profile")}'/>
            <h3>${user.name}</h3>
        </div>
        <img class="user_photo inline" src="${user.photo}">
    </div>
    <div class="prop_line">
        <div class="prop_label">Должность:</div>
        <span>${user.position}</span>
    </div>
    <div class="prop_line">
        <div class="prop_label">Отдел:</div>
        <span>${user.department.name}</span>
    </div>
    <div class="prop_line">
        <div class="prop_label">Непосредственный начальник:</div>
        <span><a href="user_profile.html">${user.userInformation.boss}</a></span>
    </div>
    <div class="prop_line">
        <div class="prop_label">E-mail:</div>
        <span><a href="mailto:shurikporohin@rambler.ru">${user.userInformation.email}</a></span>
    </div>
    <div class="prop_line">
        <div class="prop_label">Мобильный телефон:</div>
        <span>${user.userInformation.phoneMobile}</span>
    </div>
    <div class="prop_line">
        <div class="prop_label">Рабочий телефон:</div>
        <span>${user.userInformation.phoneWork}</span>
    </div>
    <div class="prop_line">
        <div class="prop_label">Внутренний телефон:</div>
        <span>${user.userInformation.phoneInternal}</span>
    </div>


</div>
<button id="main-btn">На главную</button>
</body>
</html>
