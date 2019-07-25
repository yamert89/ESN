<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 22.02.2019
  Time: 13:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <script type="text/javascript" src='<c:url value="/resources/static/props/user_props.js"/>'></script>
</head>
<body>
<c:set var="userProp" value='${user.userInformation}'/>
<div class="properties_board" data-orgurl="${sessionScope.get("org").getUrlName()}" data-login="${user.login}" data-saved="${saved}" id="user_props">
    <div class="prop_line title_pref"><h2>Настройки профиля</h2></div>
    <s:form enctype="multipart/form-data" modelAttribute="user" method="post">
    <div class="prop_line">
        <div class="inline">
            <h3 class="title_pref">${user.name}</h3>
        </div>
        <img class="user_photo inline" src='<c:url value="/resources/data${user.photo}"/>'>
        <input type="file" name="image" accept="image/*" class="inline"/>

    </div>
    <div class="prop_line">
    <div class="inline">
        <div class="prop_label">Должность:</div>
        <s:input path="position" type="text" value="${user.position}"/>
        <s:errors path="position" cssClass="jspError"/>
    </div>
    </div>
    <div class="prop_line">
    <div class="inline">
        <div class="prop_label">Отдел:</div>
        <span>${user.department.name}</span>
    </div>
    </div>
    <div class="prop_line">
    <div class="inline">
        <div class="prop_label">Дата рождения:</div><fmt:formatDate value="${userProp.birthDate.time}" pattern="yyyy-MM-dd" var="birth"/>
        <s:input path="userInformation.birthDate" type="date" id="department" value="${birth}"/>
        <s:errors path="userInformation.birthDate" cssClass="jspError"/>
    </div>
    </div>
    <div class="prop_line">
    <div class="inline">
        <div class="prop_label">Телефон:</div>
        <s:input path="userInformation.phoneMobile" type="text" autocomplete="false" value="${userProp.phoneMobile}"/>
        <s:errors path="userInformation.phoneMobile" cssClass="jspError"/>
    </div>
    </div>
    <div class="prop_line">
    <div class="inline">
        <div class="prop_label">Рабочий телефон:</div>
        <s:input path="userInformation.phoneWork" type="text" autocomplete="false" value="${userProp.phoneWork}"/>
        <s:errors path="userInformation.phoneWork" cssClass="jspError"/>
    </div>
    </div>
    <div class="prop_line">
    <div class="inline">
        <div class="prop_label">Внутренний телефон:</div>
        <s:input path="userInformation.phoneInternal" type="text" autocomplete="false" value="${userProp.phoneInternal}"/>
        <s:errors path="userInformation.phoneInternal" cssClass="jspError"/>
    </div>
    </div>
    <div class="prop_line">
    <div class="inline">
        <div class="prop_label">E-mail:</div>
        <s:input path="userInformation.email" type="text" autocomplete="false" readonly="true" onfocus="this.removeAttribute('readonly');" value="${userProp.email}"/>
        <s:errors path="userInformation.email" cssClass="jspError"/>
    </div>
    </div>
    <div class="prop_line">
    <div class="inline">
        <div class="prop_label">Непосредственный начальник:</div>
        <select name="boss" id="boss">
            <c:if test="${userProp.boss == null}"><option selected>Не указан</option></c:if>
        <c:forEach var="usr" items="${bosses}">
            <c:choose>
                <c:when test="${userProp.boss == usr}">
                    <option selected value="${usr.id}">
                </c:when>
                <c:otherwise>
                    <option  value="${usr.id}">
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test='${usr.position.equals("")}'>
                    ${usr.name}
                </c:when>
                <c:otherwise>
                    ${usr.name} - ${usr.position}
                </c:otherwise>
            </c:choose>
            </option>
        </c:forEach>
        </select>
    </div>
    </div>

    <div class="prop_line">
        <input class="commit" type="button" value="Сменить пароль" id="password_change">
    </div>
    <div class="password_board">
        <div class="inline">
            <div class="prop_label">Старый пароль:</div>
            <input type="password" id="pass_old">
        </div>
        <div class="inline">
            <div class="prop_label">Новый пароль:</div>
            <input type="password" id="pass_new">
        </div>
        <div class="inline">
            <div class="prop_label">Повторите пароль:</div>
            <input type="password" id="pass_conf">
        </div>
        <div class="inline">
            <input type="button" value="Сохранить" id="password_save">
            <input type="button" value="Отмена" id="password_cancel">
        </div>
    </div>


    <div class="prop_line">
        <input class="commit" type="submit" value="Применить Настройки" id="settings_submit"><span class="prop_log_view"></span>
    </div>
    </s:form>
    <div class="prop_line">
        <input class="commit" type="submit" value="Удалить аккаунт" id="delete_profile">
    </div>
</div>
<button id="main-btn">На главную</button>
</body>
</html>
