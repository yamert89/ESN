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
            localStorage.clear();
            sessionStorage.clear();
            var expires = "=;expires=Thu, 01 Jan 1970 00:00:00 GMT";
            document.cookie = "remember" + expires;
            document.cookie = "remember-me" + expires;
            document.cookie = "JSESSIONID" + expires;
            $.ajax({url:"/" + orgUrl + "/users/" + login, method: "delete"});
            location.href = "/reg";
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