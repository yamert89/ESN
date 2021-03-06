var stompClient = null;
var orgUrl = window.orgUrl;
function Group(name, expanded){
    this.name = name;
    this.expanded = expanded;
}

function checkNewMessages(){
    console.log("check new mes");
    if (sessionStorage.getItem("gen") == "on") $("#chat").find("img").css("display", "inline-block");
    $(".person_item").each(function () {
        if (sessionStorage.getItem("private" + $(this).attr("data-id")) == "on") $(this).children().first().next().css("display", "inline-block");
    });
}

$(document).ready(function () {
    var date = new Date();
    console.log('contacts ready ' + new Date().getSeconds() + ':' + date.getMilliseconds());


    $(document).on('click','.person_item', function(){
        $(this).addClass('selected');
        var login = $(this).attr("data-login");

        //$.get("/" + orgUrl + "/private-chat", {companion: login});
        window.callFrame("/" + orgUrl + "/private-chat/" + login);

    });
    $(document).on('click', '.multicast_icon', function (event) {
        event.preventDefault();
        var message = prompt('Отправка сообщения участникам группы', '');
        if (message == null)  return;
        var groupName = this.previousSibling.innerHTML;
        $.ajax({url : "/groupmessage", type: "post", data: {text : message, groupName : groupName}});
        var currentCompanion = $('.contacts-frame').contents().find('.selected');
        if (currentCompanion.length > 0 &&
            currentCompanion.parent().children().first()
                .find($(".group_name")).text() === groupName) $('.private_chat_container')
            .prepend('<div class="private_chat comment_bubble_right"><div class="time-left">' +
                getDate(new Date()) + ' </div>' + message + ' </div>');
    });

    $.ajax({url:"/" + orgUrl + "/contacts",
        type:"GET", contentType:"application/json; charset=UTF-8", success:contactsFill, error : err});

    //setTimeout('f()',2000)

    $(window).on("beforeunload", function () {
        var groups = [];
        var details = $("details");
        if (details.length < 2) return;
        details.each(function (idx, el) {
            groups.push(new Group(el.firstChild.firstChild.innerHTML, el.hasAttribute("open")))
        });
        $.ajax({url : "/expand-props", data : {groups : JSON.stringify(groups)}});
        //disconnectWS();
    });



});

function f() {
    stompClient.send("/app/netstatus", {}, JSON.stringify({"userId":48, "statusOn": true}));
}


function err(ex, stat) {
    window.notify('Ошибка на сервере ' + stat);
}


function contactsFill(data) {
    if (!$.isArray(data)) {
        renderGroup(data);
        if (data.newSession) {
            localStorage.clear();
            sessionStorage.clear();
            console.log("clear LS with contacts");
        }
    }else {
        if (data[0].newSession) {
            localStorage.clear();
            sessionStorage.clear();
            console.log("clear LS with contacts");
        }
        data.forEach(function (item) {
            renderGroup(item);
        });
    }

    $(".contacts-frame").children().last().remove();
    checkNewMessages();
}

function renderGroup(group) {
    var body = $(".contacts-frame");
    var expand = group.expanded ? " open=\"open\"" : "";
    var details = '<details' + expand + '><summary data-><span class="group_name">' + group.name +
        '</span><img src="/resources/data/app/multicast_message.png" class="multicast_icon" title="Многоадресное сообщение"></summary>';
    for (var i = 0; i < group.users.length; i++){
        details += renderItem(group.users[i]);
    }
    details += '</details>';
    body.append(details);
    body.append("<hr color='a9a9a9' size='1'>");
}

function renderItem(user) {
    var idStat = user.status ? 'net_status_on' : 'net_status_off';
    var divContent = user.role == "ROLE_ADMIN" ? "class='person_item admin' title='Администратор'" : "class='person_item'";
    return "<div " + divContent + " data-id='" + user.id + "' data-login='" + user.login +
        "'><div class='net_status_circle' id='" + idStat + "'></div><img src='/resources/data/app/new_message.png' class='new_priv_mes'><div class='person_name'>" + user.name + "</div></div>"
}
