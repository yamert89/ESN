<%@ taglib prefix="s" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 16.05.2019
  Time: 13:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Настройки профиля</title>
    <script type="text/javascript" src='<c:url value="/resources/static/props/org_props.js"/>'></script>
</head>
<body><c:set value='${sessionScope.get("org")}' var="org" />
<div class="properties_board" data-id='${org.id}' data-url="${org.urlName}" data-saved="${saved}" id="org_props">
    <div class="prop_line title_pref"><h2>Настройки</h2></div>
    <s:form enctype="multipart/form-data" modelAttribute="org" method="post">
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label">Название:</div>
                <s:input path="name" type="text" value="${org.name}" data-old="${org.name}"/>
                <s:errors path="name" cssClass="jspError"/>
            </div>
        </div>
        <div><hr></div>
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label">Относительный URL:</div>
                <s:input path="urlName" type="text" value="/${org.urlName}" readonly="true" cssClass="urlName"/>
                <s:errors path="urlName" cssClass="jspError"/>
            </div>
        </div>
        <div><hr></div>
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label">Фон хэдера:</div>
                <input type="file" name="header" accept="image/*" class="inline"/>
            </div>
        </div>
        <div><hr></div>
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label">Перечень должностей:</div>
                <div class="positions">
                    <c:if test="${org.positions.size() > 0}">
                        <c:forEach var="pos" items="${org.positions}">
                            <div class="position">
                                <div class="position_name">${pos}</div>
                                <img class="position_delete" src="<c:url value="/resources/data/app/cross.png"/>" title="Удалить должность"/>
                            </div>
                        </c:forEach>
                    </c:if>

                </div>
            </div>
            <div class="inline">
                <button type="button" class="position_add_button">Добавить должность:</button>
                <input class="position_add_input" name="pos" type="text"/>
            </div>
        </div>
        <div><hr></div>
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label fixed_max">Ключ администратора:</div>
                <input type="text" readonly value="${org.adminKey}" title="Необходим при регистрации пользователя с правами администратора" id="adminKey" size="60" minlength="60"/>
                <button type="button" onclick="copy('#adminKey')">Скопировать</button>
            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label fixed_max">Общий корпоративный ключ:</div>
                <input type="text" readonly value="${org.corpKey}" title="Необходим при регистрации пользователей" id="corpKey" size="60" minlength="60"/>
                <button type="button" onclick="copy('#corpKey')">Скопировать</button>
            </div>
        </div>
        <div><hr></div>
        <div class="prop_line">
            <input class="commit" type="submit" value="Применить Настройки" id="settings_submit" disabled><span class="prop_log_view"></span>
        </div>
    </s:form>
    <div><hr></div>
    <div class="prop_line">
        <input class="commit" type="button" value="Удалить профиль" id="delete_profile" title="Удаление профиля организации. В течение месяца его можно будет восстановить, используя корпоративный ключ">
    </div>


</div>
<button id="main-btn">На главную</button>
</body>
</html>
