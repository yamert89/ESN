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
    <link rel="stylesheet" href="prefs.css">
    <script type="text/javascript" src="../../libs/jquery_3.1.0.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            var propLog = $(".prop_log_view");
            var pasBoard = $(".password_board");
            var passOld = $("#pass_old");
            var passNew = $("#pass_new");
            var passConf = $("#pass_conf");
            $("#password_change").click(function () {
                pasBoard.css("display", "block");
            });
            passOld.on('blur', function () {
                if ($(this).val() == ''){
                    logMess("Введите пароль");
                    return;
                }
                checkPassword();

            });
            $("#password_save").click(function () {
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
                $.ajax({url:"", type: "post", data:{newPass : newPass, oldPass : oldPass},
                    async : false, success: successChangePassword, contentType: "application/json; charset=UTF-8"});
            });

            $("#delete_profile").click(function () {
                if(confirm("Вы действително хотите удалить аккаунт? Все связанные данные будут удалены без возможности восстановления")){
                    alert("DELETING");
                    var login = "" //TODO получить логин loginUrl
                    $.ajax({url:"/" + orgUrl + "/" + login, type: "delete"});
                }
            });

            $("#settings_submit").click(function () {
                logMess("Сохранено");
                //TODO save on server
            });

            function successChangePassword(data) {
                if (!data) {
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
                }, 3000);
            }

            function checkPasswordResult(data){
                alert(data);
                if (!data) logMess("Существующий пароль не совпадает");
            }

            function checkPassword() {
                $.ajax({url:"/password", contentType:"application/json; charset=UTF-8", success: checkPasswordResult});
            }

            function checkPasswordSync() {
                $.ajax({url:"/password", contentType:"application/json; charset=UTF-8", success: checkPasswordResult});
            }

        });
    </script>
</head>
<body>
<div class="properties_board">
    <div class="prop_line title_pref"><h2>Настройки профиля</h2></div>

    <div class="prop_line inline_parent">
        <div class="inline">
            <h3>Порохин Александр Акимович</h3>
        </div>
        <img class="user_photo inline" src="../../avatars/wom.jpg">
        <input type="file" accept="image/*" class="inline">
    </div>
    <div class="prop_line">
        <div class="prop_label">Дата рождения:</div>
        <input type="date" id="department">
    </div>

    <div class="prop_line">
        <div class="prop_label">Телефон:</div>
        <input type="text">
    </div>
    <div class="prop_line">
        <div class="prop_label">Рабочий телефон:</div>
        <input type="text">
    </div>
    <div class="prop_line">
        <div class="prop_label">Внутренний телефон:</div>
        <input type="text">
    </div>
    <div class="prop_line">
        <div class="prop_label">E-mail:</div>
        <input type="text">
    </div>
    <div class="prop_line">
        <div class="prop_label">Должность:</div>
        <input type="text">
    </div>
    <div class="prop_line">
        <div class="prop_label">Отдел:</div>
        <input type="text">
    </div>
    <div class="prop_line">
        <div class="prop_label">Непосредственный начальник:</div>
        <select>
            <option>Чубака Иван Иванович - начальник отдела</option>
        </select>
    </div>
    <div class="prop_line">
        <input class="commit" type="button" value="Сменить пароль" id="password_change">
    </div>
    <div class="password_board">
        <div class="prop_line">
            <div class="prop_label">Старый пароль:</div>
            <input type="password" id="pass_old">
        </div>
        <div class="prop_line">
            <div class="prop_label">Новый пароль:</div>
            <input type="password" id="pass_new">
        </div>
        <div class="prop_line">
            <div class="prop_label">Повторите пароль:</div>
            <input type="password" id="pass_conf">
        </div>
        <div class="prop_line">
            <input type="button" value="Сохранить" id="password_save">
        </div>
    </div>

    <div class="prop_line">
        <input class="commit" type="submit" value="Применить Настройки" id="settings_submit">
    </div>
    <div class="prop_line">
        <input class="commit" type="submit" value="Удалить аккаунт" id="delete_profile">
    </div>
    <span class="prop_log_view"></span>
</div>
</body>
</html>
