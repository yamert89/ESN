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
    <script type="text/javascript">
        $(document).ready(function () {
            var board = $(".properties_board");
            var login = board.attr("data-login");
            var orgUrl = board.attr("data-orgurl");
            var propLog = $(".prop_log_view");
            var pasBoard = $(".password_board");
            var passOld = $("#pass_old");
            var passNew = $("#pass_new");
            var passConf = $("#pass_conf");
            var commitBtns = $(".commit");
            if (board.attr('data-saved') == 'true') logMess("Сохранено");
              
            $("#password_change").click(function () {
                pasBoard.css("display", "block");
                commitBtns.attr("disabled", true);
                window.scrollBy(0, 300);

            });
            passOld.on('blur', function () {
                var pass = $(this).val();
                if (pass == ''){
                    logMess("Введите пароль");
                    return;
                }
                checkPassword(pass);

            });
            $("#password_save").click(function (e) {
                var newPass = passNew.val();
                var oldPass = passOld.val();

                if (passConf.val() != passNew.val()) {
                    logMess("Новый и подтвержденный пароли не совпадают");
                    return;
                }
                if (newPass == "") return;
                if (passNew.val().length < 6) {
                    logMess("Введите не менее 6 символов");
                    return;
                }
                $.ajax({url:"/" + orgUrl + "/users/" + login + "/p", type: "POST", dataType: "json", data:{newPass:newPass,oldPass:oldPass},
                    complete: successChangePassword});


            });

            $("#delete_profile").click(function () {
                if(confirm("Вы действително хотите удалить аккаунт? Все связанные данные будут удалены без возможности восстановления")){
                    alert("DELETING");

                    $.ajax({url:"/" + orgUrl + "/users/" + login, type: "delete"});
                }
            });


            $("#password_cancel").click(function () {
                pasBoard.css("display", "none");
                commitBtns.removeAttr("disabled");
            });

            $("#main-btn").click(function () {
                location.href = "/" + orgUrl + "/wall/"
            });


            function successChangePassword(data) {
                if (!data.responseJSON) {
                    logMess("Старый пароль введен неверно");
                    return
                }
                pasBoard.css("display", "none");
                passOld.val('');
                passNew.val('');
                passConf.val('');
                logMess("Пароль изменен. Пожалуйста, авторизуйтесь заново.");
                setTimeout(function () { location.href = "/" + orgUrl + "/auth1"}, 2000);

            }

            function logMess(message) {
                propLog.text(message);
                propLog.addClass("prop_log_fadeout");
                setTimeout(function () {
                    propLog.text("");
                    propLog.removeClass("prop_log_fadeout");
                }, 2500);
            }

            function checkPasswordResult(data){
                if (!data) logMess("Существующий пароль не совпадает");
            }

            function checkPassword(password) {
                $.ajax({url:"/" + orgUrl + "/users/" + login + "/p", contentType:"application/json; charset=UTF-8",
                    success: checkPasswordResult, data: {pass : password}});
            }

            function checkPasswordSync() {
                $.ajax({url:"/password", contentType:"application/json; charset=UTF-8", success: checkPasswordResult});
            }

            function err(ex, stat) {
                alert(ex + stat);
            }

            /*$(document).on('submit', function () {
                /!*var bossSelect = $("#boss");
                var bossName = bossSelect.val();
                var bossId = bossSelect.find("option:contains('" + bossName + "')").first().val();
                bossSelect.val(bossId);*!/
            });*/


        });
    </script>
</head>
<body>
<c:set var="userProp" value='${user.userInformation}'/>
<div class="properties_board" data-orgurl="${sessionScope.get("org").getUrlName()}" data-login="${user.login}" data-saved="${saved}">
    <div class="prop_line title_pref"><h2>Настройки профиля</h2></div>
    <s:form enctype="multipart/form-data" modelAttribute="user" method="post">
    <div class="prop_line">
        <div class="inline">
            <h3 class="title_pref">${user.name}</h3>
        </div>
        <img class="user_photo inline" src='<c:url value="/resources/avatars/${user.photo}"/>'>
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
