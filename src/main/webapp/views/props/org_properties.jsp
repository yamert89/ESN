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
        $(document).ready(function () {
           $('.position_add_button').click(addPosition);
           $(document).on('click', '.position_delete', function () {
               $(this).parent().remove();
           });
            $(document).keypress(function (event) {
                if (event.which === 13 && $('.position_add_input').is(':focus')) { //TODO enter run on form
                    addPosition();
                }
            });
            $(document).on('submit', function () {
                var str = '';
                $('.position').each(function (idx, el) {
                    el = el.getElementsByClassName('position_name')[0];
                    str += "@@@" + el.innerHTML;
                });
                console.log(str);
                $('.position_add_input').val(str);
                return false;

            })
        });
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

        function addPosition() {
            $('.positions').append('<div class="position">\n' +
                '                            <div class="position_name">' + $('.position_add_input').val() + '</div>\n' +
                '                            <img class="position_delete" src="/resources/cross.png" title="Удалить должность"/>\n' +
                '                    </div>')
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
        <div><hr></div>
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label">Фон хэдера:</div>
                <input type="file" name="image" accept="image/*" class="inline"/>
            </div>
        </div>
        <div><hr></div>
        <div class="prop_line">
            <div class="inline">
                <div class="prop_label">Перечень должностей:</div>
                <div class="positions">
                    <c:forEach var="pos" items="${organization.positions}">
                    <div class="position">
                            <div class="position_name">${pos}</div>
                            <img class="position_delete" src="<c:url value="/resources/cross.png"/>" title="Удалить должность"/>
                    </div>
                    </c:forEach>
                </div>
            </div>
            <div class="inline">
                <button type="button" class="position_add_button">Добавить должность:</button>
                <input class="position_add_input"/>
            </div>
        </div>
        <div><hr></div>
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
        <input class="commit" type="submit" value="Удалить профиль" id="delete_profile" title="Удаление профиля организации. В течение месяца его можно будет восстановить, используя корпоративный ключ">
    </div>

    <span class="prop_log_view"></span>
</div>
<button id="main-btn">На главную</button>
</body>
</html>
