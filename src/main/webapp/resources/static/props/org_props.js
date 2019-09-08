var positionChange = false;
$(document).ready(function () {
    var board = $(".properties_board");
    var propLog = $(".prop_log_view");
    if (board.attr('data-saved') == 'true') logMess("Сохранено");
    var orgUrl = $(".properties_board").attr('data-url');
    $('.position_add_button').click(addPosition);
    $(document).on('click', '.position_delete', function () {
        $(this).parent().remove();
        activateCommit();
    });
    $(document).keypress(function (event) {
        if (event.which === 13) {
            if ($('.position_add_input').is(':focus')) addPosition();
            event.preventDefault();
        }
    });
    $(document).on('submit', function (event) {
        var name = $("input[name=name]");
        if(name.val() == name.attr("data-old")  &&
            $("input[name=header]").val().length < 1 &&
            !positionChange) {
            event.preventDefault();
            return;
        }
        var urlName = $(".urlName");
        var val = urlName.val();
        if (val[0] == "/") urlName.val(val.substr(1));
        var str = '';
        $('.position').each(function (idx, el) {
            el = el.getElementsByClassName('position_name')[0];
            str += ", " + el.innerHTML;
        });
        console.log(str);
        $('.position_add_input').val(str.substr(2));



    });

    $("#delete_profile").click(function () {
        var res = confirm('Вы уверены, что хотите удалить профиль организации? В течение месяца его можно будет восстановить, используя корпоративный ключ');

        if (res) {
            var id = $(".properties_board").attr('data-id');
            $.get("/delete_org");
        }
    });

    $("#main-btn").click(function () {
        location.href = "/" + orgUrl + "/wall/"
    });

    function logMess(message) {
        propLog.text(message);
        propLog.addClass("prop_log_fadeout");
        setTimeout(function () {
            propLog.text("");
            propLog.removeClass("prop_log_fadeout");
        }, 2500);
    }
});
function copy(selector) {
    var el = document.querySelector(selector);
    el.focus();
    el.select();
    try {
        var successful = document.execCommand('copy');
        var msg = successful ? 'successful' : 'unsuccessful';
        console.log('Copying text command was ' + msg);
    } catch (err) {
        console.log('Oops, unable to copy');
    }
}

function addPosition() {
    var name = $('.position_add_input').val();
    if (name == '') return;
    $('.positions').append('<div class="position">\n' +
        '                            <div class="position_name">' + name + '</div>\n' +
        '                            <img class="position_delete" src="/resources/data/app/cross.png" title="Удалить должность"/>\n' +
        '                    </div>');
    positionChange = true;
    activateCommit();
}