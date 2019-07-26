var stompClient = null;
var orgUrl = window.orgUrl;
function Group(name, expanded){
    this.name = name;
    this.expanded = expanded;
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
        $.ajax({url : "/groupmessage", type: "post", data: {text : message, groupName : groupName}})

    });

    $.ajax({url:"/" + orgUrl + "/contacts",
        type:"GET", contentType:"application/json; charset=UTF-8", success:contactsFill, error : err});

    //setTimeout('f()',2000)

    $(window).on("beforeunload", function () {
        var groups = [];
        $("details").each(function (idx, el) {
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
    }else {
        data.forEach(function (item) {
            renderGroup(item);
        });
    }

    $(".contacts-frame").children().last().remove();
}

function renderGroup(group) {
    var body = $(".contacts-frame");
    var expand = group.expanded ? " open=\"open\"" : "";
    var details = '<details' + expand + '><summary data-><span class="group_name">' + group.name +
        '</span><img src="/resources/multicast_message.png" class="multicast_icon" title="Многоадресное сообщение"></summary>';
    for (var i = 0; i < group.users.length; i++){
        details += renderItem(group.users[i]);
    }
    details += '</details>';
    body.append(details);
    body.append("<hr color='a9a9a9' size='1'>");
}

function renderItem(user) {
    var idStat = user.status ? 'net_status_on' : 'net_status_off';
    return "<div class='person_item' data-id='" + user.id + "' data-login='" + user.login +
        "'><div class='net_status_circle' id='" + idStat + "'></div><img src='/resources/new_message.png' class='new_priv_mes'><div class='person_name'>" + user.name + "</div></div>"
}