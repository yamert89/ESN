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
    <title>Title</title>
    <script type="text/javascript">
        function copy() {
            var el = document.querySelector('#key');
            el.focus();
            el.select();
            //document.execCommand('copy');
            try {
                var successful = document.execCommand('copy');
                var msg = successful ? 'successful' : 'unsuccessful';
                console.log('Copying text command was ' + msg);
            } catch (err) {
                console.log('Oops, unable to copy');
            }
        }
    </script>
</head>
<body>
<div class="properties_board">
    <div class="prop_line title_pref"><h2>Настройки</h2></div>
    <s:form enctype="multipart/form-data" modelAttribute="organization" method="post">
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label">Название:</div>
                <s:input path="name" type="text" value="${organization.name}"/>
                <s:errors path="name" cssClass="jspError"/>
            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label">Фон хэдера:</div>
                <input type="file" name="image" accept="image/*" class="inline"/>
            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label">Перечень должностей:</div>
                <sp class="positions">
                    <c:forEach var="pos" items="${organization.positions}">
                    <span class="position">${pos}</span><img src="" />
                    </c:forEach>
                </div>
            </div>
            <div class="inline">
                <button>Добавить должность:</button>
                <input/>
            </div>
        </div>
        <div class="prop_line">
            <div class="inline">

            </div>
        </div>
        <div class="prop_line">
            <div class="inline">

            </div>
        </div>
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label">Корпоративный ключ:</div>
                <input type="text" readonly value="${organization.key}" title="Необходим при регистрации пользователей" id="key" size="60" minlength="60"/>
                <button type="button" onclick="copy()">Скопировать</button>
                <s:errors path="key" cssClass="jspError"/>
            </div>
        </div>


        <div class="prop_line">
            <input class="commit" type="submit" value="Применить Настройки" id="settings_submit">
        </div>
    </s:form>
    <div class="prop_line">
        <input class="commit" type="submit" value="Удалить аккаунт" id="delete_profile">
    </div>

    <span class="prop_log_view"></span>
</div>
<button id="main-btn">На главную</button>
</body>
</html>
