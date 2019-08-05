var myTree = null;

function DayWithText(day){
    this.day = day;
    this.notes = [];
}

window.listOfDatesNoted = [];
$(document).ready(function () {
    if (sessionStorage.getItem("gen") == "on") $("#chat").find("img").css("display", "inline-block");
    $(".person_item").each(function () {
        if (sessionStorage.getItem("private" + $(this).attr("data-id")) == "on") $(this).children().first().next().css("display", "inline-block");

    });

    $(".problem").click(function () {
       var mes = prompt("Введите текст сообщения", "");
       if (!mes == "") {
           $.post("/problem", {message : mes});
           notify("Сообщение отправлено");
       }

    });



    var date = new Date();
    console.log('index ready ' + new Date().getSeconds() + ':' + date.getMilliseconds());
    var uName = $(".user_name");
    var uTitle = $(".title");

    window.userName = uName.text();
    window.login = uName.attr("data-login");
    window.userId = uName.attr("data-user_id");
    window.orgName = uTitle.text();
    window.orgUrl = uTitle.attr("data-url");
    window.orgId = uTitle.attr("data-org-id");
    //data-org-url
    window.eventDates = [];
    window.datesArray = [];
    window.inited = false;

    $(".tool").click(function () {
       startProgress();
       location.href = '/' + orgUrl + '/' + $(this).attr('id');
    });

    $(document).ajaxError(function(event, jqXHR, ajaxSettings, thrownError) {
        notify("Запрос к серверу завершился с ошибкой");

        console.log(event);
        console.log(jqXHR);
        console.log(ajaxSettings);
        console.log(thrownError);
        $.post('/clienterror', {event : event, jqXHR : jqXHR, ajaxSettings : ajaxSettings, thrownError : thrownError});
    });

    //$.get("/notes", null, datepickerInit, "json");

    $.ajax({url:"/notes", success: datepickerInit, error: err, dataType : "json"});


    uName.click(function () {
        props();
    });

    $(".user_photo").click(function () {
        props();
    });

    /*$(".tool").click(function () {
        $(".tool").removeClass("selected");
        $(this).addClass("selected")
    });*/



    function props() {
        location.href = "/" + orgUrl + "/users/" + login;
    }



    function datepickerInit(data){




        //window.dates = [{m:1, d:13, t:"text1"}, {m:2, d:1, t:"text2"}, {m:2, d:12, t:"text3"}];
        var $picker = $('.datepicker-here');
        var pickerObj = $picker.data('datepicker');
        var monthNumber = pickerObj.loc.months.indexOf($picker.find('.datepicker--nav-title').text().split(',')[0]);

        window.dates = data;
        window.datesOfThisMonth = [];

        var thisYear = new Date().getFullYear();
        window.year = thisYear;
        window.dates.forEach(function (el) {
            if (el.m == monthNumber) window.datesOfThisMonth.push(el);
        });




        window.eventDates = getEventDates(monthNumber);






        $picker.datepicker({
            onRenderCell: function (date, cellType) {

                var currentDate = date.getDate();

                // Добавляем вспомогательный элемент, если число содержится в `eventDates`
                if (cellType == 'day' && window.eventDates.indexOf(currentDate) != -1) {
                    return {
                        html: currentDate + '<span class="dp-note"></span>'
                    }
                }

            },
            onSelect: function onSelect(fd, date) {
                var title = '', content = '';

                var monthNumber = pickerObj.loc.months.indexOf(
                    $picker.find('.datepicker--nav-title').text().split(',')[0]);
                var notes = [];
                var text = '';
                var idx = 1;
                window.datesOfThisMonth.forEach(function (value) {
                    if (value.d == date.getDate()) {
                        text = text + idx + ') ' + value.t  + '\n';
                        ++idx;
                    }
                });


                var note = prompt(text + '\n' + "Добавить новую заметку?", "");
                if (note == null) return;
                var now = new Date();
                date.setHours(now.getHours(), now.getMinutes(), now.getSeconds(), now.getMilliseconds());

                $.ajax({type:"POST", url:"/note", data:{time:getDate(date), text:note, success: function (data) {
                            setTimeout(function () {
                                location.reload();
                            }, 500)

                        }}});


            },
            onChangeMonth: function (monthN, year) {
                if (year != thisYear) {
                    window.eventDates = [];
                    return;
                }

                window.eventDates = getEventDates(monthN);
                window.dates.forEach(function (el) {
                    if (el.m == monthN) window.datesOfThisMonth.push(el);
                });
                var pickerObj = $picker.data('datepicker');
                pickerObj.update('minDate');
            },

            moveToOtherMonthsOnSelect:false
        });

    }

    connectWS();





    /* $("#groups").click(function () {
         location.href = "/groups";
     });*/


});



function connectWS() {
    var socket = new SockJS('/messages');
    var stompClient = Stomp.over(socket);

    var subscribePrefix = "/user/" + userId;
    stompClient.connect({}, function(frame) {

        stompClient.subscribe('/allusers' + orgId , function(data){
            var resp = JSON.parse(data.body);
            //if (resp.initiatorId == userId) return;
            switch (resp._alert) {
                case 'genmessage':
                    if (resp.initiatorId == userId) return;
                    if (!$("#chat").hasClass("selected")) $("#chat_m").css("display", "block");
                    else {
                        renderMessage(resp.mes , 'mailing');
                        //location.reload();
                    }
                    break;
                case 'post':
                    if (resp.initiatorId == userId) return;
                    if (!$("#wall").hasClass("selected")) $("#wall_m").css("display", "block");
                    else {
                        renderPost(resp.mes, 'mailing');
                        //location.reload();
                    }
                    break;
                case 'netstatus':
                    var user = JSON.parse(data.body);
                    var usDom = $(".contacts-frame").find("[data-id='" + user.initiatorId + "']");
                    var stat = user.statusOn ? 'net_status_on' : 'net_status_off';
                    usDom.find(":first-child").attr("id", stat);

                    var netStatus = $(".person_photo_chat").next(); //TODo test
                    netStatus.attr("id", stat);
                    netStatus.next().text(user.statusOn ? "в сети" : "не в сети");

                    break;

            }
        });



        stompClient.subscribe(subscribePrefix + '/message', function (data) {
            var resp = JSON.parse(data.body);
            switch (resp.type) {
                case 'private':
                    var currentCompanion = $('.contacts-frame').contents().find('.selected');

                    if (currentCompanion.length > 0 && currentCompanion.attr("data-id") == resp.senderId){
                        $('.private_chat_container').prepend('<div class="private_chat comment_bubble_left"><div class="time-left">' +
                            getDate(new Date()) + ' </div>' + resp.text + ' </div>');
                        stompClient.send("/app/readprivate", {}, JSON.stringify({senderId : resp.senderId, hash : hash(resp.text)}));

                    }else {
                        privateMessageAlert(resp.senderId);
                    }
                    break;
                case 'private_alert_read_one':
                    $(".private_chat_container").find(".unreaded").each(function () {
                        var text = $(this).get(0).innerText;
                        var startIdx = text.indexOf('\n');
                        var elHash = hash(text.substr(++startIdx));
                        if (resp.hash == elHash) {
                            $(this).removeClass("unreaded");

                        }
                    });
                    break;
                case 'private_alert_read_all':
                    $(".private_chat_container").find(".unreaded").removeClass("unreaded");
                    break;
                case 'new_messages':
                    console.log(resp.gen);
                    console.log(resp.private_ids);
                    if (resp.gen) {
                        $("#chat_m").css("display", "inline-block");
                        sessionStorage.setItem("gen", "on");
                    }
                    if (resp.private) resp.private_ids.forEach(function (el) {
                        privateMessageAlert(el);
                        sessionStorage.setItem("private" + el, "on");
                    });




            }


        });

        if (!sessionStorage.getItem("reloaded")){
            sendNewMReq();
            sessionStorage.setItem("reloaded", "true");
            //console.log("write first cookie");
            function sendNewMReq() {
                try{
                    setTimeout(function () {
                        //console.log("new");
                        stompClient.send("/app/messages", {us : userId}, orgId.toString());
                    }, 200)
                }catch (e) {
                    //console.log("try reconnection...");
                    sendNewMReq();
                }
            }
        }



    } );


}

function privateMessageAlert(senderId){
    $(".contacts-frame").find("[data-id=" + senderId + "]").each(function () {
        $(this).children().first().next().css("display", "inline-block") ;
    })
}


function getFilteredArray(timeUnit){
    switch (timeUnit) {
        case "d":
            break;
    }

}


function getEventDates(month){
    var arr = [];
    window.dates.forEach(function (el) {
        var v = el.m;
        if(v == month){
            arr.push(el.d);


        }
    });
    return arr;
}


function dateOfNoteColorize() {
    window.listOfDatesNoted.forEach(function (value) {
        var el34 = $(".datepicker--cells").children("[data-date=\'" + value[0] + "\']" +
            "[data-month=\'" + value[1] + "\']" +
            "[data-year=\'" + value[2] + "\']");
        el34.addClass("date_noted");
    });
}

function setCurrentDate(el) {
    el.text(getCurrentDate());
}

function getDate(date) {
    var options = {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
    };
    return prepUnit(date.getHours()) + ':' + prepUnit(date.getMinutes()) + ':' +
        prepUnit(date.getSeconds()) + ' / ' + prepUnit(date.getDate()) + '.' + prepUnit(date.getMonth() + 1);

}

function prepUnit(unit) {
    return unit < 10 ? '0' + unit : unit;
}

var callFrame = function(link) {
    location.href = link;
}

function err(t, e) {
    alert(t + ' ' + e);
}

function updatePage() {
    location.reload();
}

window.notify = function(text, time) {
    if (time == undefined) time = 3000;
    var log = $(".log");
    log.html('<span class="log_text">' + text + '</span>');
    log.addClass('show_log');
    setTimeout(function () {
        log.removeClass('show_log');
    }, time)

};


function hash(input){
    var arr = input.split("");
    var res = 0;
    arr.forEach(function(el, idx){
        if(idx > 15) return;
        res += el.codePointAt();
    });
    return res;
}

function getCookie(name){
    console.log("cookies : " + document.cookie);
    var matches = document.cookie.match((new RegExp(name + "=[^;]*")));
    var res = matches != null ? matches.toString().replace(/.*=/, "") : null;
    console.log("get cookie : " + res);
    return res;
}

/*var getf = function () {
    $.ajax({url:"http://localhost:8080/" + orgUrl + "/contacts",
        type:"GET", contentType:"application/json; charset=UTF-8", success:contactsFill, error : err});
}*/



/*$(function(){
        $('#exampleModal').arcticmodal();

    });*/