<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<header>
    <link rel="stylesheet" href='<c:url value="/resources/static/index.css"/>'>
    <link rel="stylesheet" href='<c:url value="/resources/static/gen.css"/>'>
    <link rel="stylesheet" href='<c:url value="/resources/static/tools/tools.css"/>'>
    <link rel="stylesheet" href='<c:url value="/resources/static/center/notes/notes.css"/>'>
    <link rel="stylesheet" href='<c:url value="/resources/libs/air-datepicker-master/dist/css/datepicker.css"/>'>
    <script src='<c:url value="/resources/libs/jquery_3.1.0.js"/>'></script>
    <script src='<c:url value="/resources/libs/air-datepicker-master/dist/js/datepicker.js"/>'></script>
    <script src='<c:url value="/resources/libs/sock.js"/>'></script>
    <script src='<c:url value="/resources/libs/stomp.js"/>'></script>
    <script src="<c:url value="/resources/libs/ckeditor/ckeditor.js"/>"></script>
    <script type="text/javascript">
        var myTree = null;

        function DayWithText(day){
            this.day = day;
            this.notes = [];
        }

        window.listOfDatesNoted = [];
        $(document).ready(function () {
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

            $.get("/notes", null, datepickerInit, "json");

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
                        //var showText = window.dates[]; //TODO поиск текста и показ

                       // alert(text);

                        var note = prompt(text + '\n' + "Добавить новую заметку?", "");
                        if (note == null) return;
                        var now = new Date();
                        date.setHours(now.getHours(), now.getMinutes(), now.getSeconds(), now.getMilliseconds());

                        $.ajax({type:"POST", url:"/note", data:{time:getDate(date), text:note, success: function (data) {
                                    setTimeout(function () {
                                        location.reload();
                                    }, 500)

                                }}});
                        //TODO


                        // Если выбрана дата с событием, то отображаем его
                        /*if (date && eventDates.indexOf(date.getDate()) != -1) {

                        }*/

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
            //if (getCookie("first") == null) ////TODO connect only first load
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

                stompClient.subscribe('/genchat' + orgId , function(data){
                    var resp = JSON.parse(data.body);
                    //if (resp.initiatorId == userId) return;
                    switch (resp._alert) {
                        case 'genmessage':
                            if (!$("#chat").hasClass("selected")) $("#chat_m").css("display", "block");
                            else {
                                renderMessage(resp.mes , 'mailing');
                                //location.reload();
                            }
                            break;
                        case 'post':
                            if (!$("#wall").hasClass("selected")) $("#wall_m").css("display", "block");
                            else {
                                renderPost(resp.mes, 'mailing');
                                //location.reload();
                            }
                            break;
                        case 'netstatus':
                            var user = JSON.parse(data.body);
                            var usDom = $(".contacts-frame").find("[data-id='" + user.userId + "']");
                            var stat = user.statusOn ? 'net_status_on' : 'net_status_off';
                            usDom.find(":first-child").attr("id", stat);
                            break;

                    }
                });



                stompClient.subscribe(subscribePrefix + '/message', function (data) {
                    var resp = JSON.parse(data.body);
                    switch (resp.type) {
                        case 'private':
                            var currentCompanion = $('.contacts-frame').contents().find('.selected');

                            if (currentCompanion.length > 0 && currentCompanion.attr("data-id") === resp.senderId){
                                $('.private_chat_container').prepend('<div class="private_chat comment_bubble_left"><div class="time-left">' +
                                    getDate(new Date()) + ' </div>' + resp.text + ' </div>');
                                stompClient.send("/app/messages", {}, JSON.stringify({senderId : resp.senderId, hash : hash(resp.text)}));

                            }else {
                               privateMessageAlert(resp.senderId);
                            }
                            break;
                        case 'private_alert_read':
                            $(".private_chat_container").find(".unreaded").each(function () {
                                var elHash = hash($(this).children().get(1)); //TODO test
                                if (resp.hash == elHash) {
                                    $(this).removeClass("unread");
                                    break;
                                }
                            });
                            break;
                        case 'new_messages':
                            console.log(resp.gen);
                            console.log(resp.private_ids);
                            if (resp.gen) $("#chat_m").css("display", "block");
                            if (resp.private) resp.private_ids.forEach(function (el) {
                                privateMessageAlert(el);
                            })


                    }


                });

                if (getCookie("first") == null) {
                    sendNewMReq();
                    document.cookie = "first=true; path=/";
                    console.log("write first cookie");
                    function sendNewMReq() {
                        try{
                            setTimeout(function () {
                                console.log("new");
                                stompClient.send("/app/messages", {us : userId}, orgId.toString());
                            }, 200)
                        }catch (e) {
                            console.log("try reconnection...");
                            sendNewMReq();
                        }
                    }
                }

            } );








        }

        function privateMessageAlert(senderId){
            $(".contacts-frame").contents().find("[data-id=" + senderId + "]").each(function () {
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
        
        function notify(text) {
            var log = $(".log");
            log.html('<span class="log_text">' + text + '</span>');
            log.addClass('show_log');
            setTimeout(function () {
                log.removeClass('show_log');
            }, 3000)

        }


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



        /*$(function(){
                $('#exampleModal').arcticmodal();

            });*/

    </script><c:set var="user" value='${sessionScope.get("user")}'/>
    <div class="title" align="center" data-url='${sessionScope.get("orgUrl")}' data-org-id='${sessionScope.get("orgId")}'>${user.organization.name}</div>
    <div class="user">
        <span class="user_name" data-login="${user.login}" data-user_id="${user.id}">${user.name}</span>
        <img src='<c:url value="/resources/avatars/${user.photo}"/>' class="user_photo">
    </div>
</header>
<body>
<div class="container">
    <div class="tools"><c:set var="orgUrl" value='${sessionScope.get("orgUrl")}'/>
        <div class="tool" id="wall" onclick="location.href = '/${orgUrl}/wall'">Лента
            <img src='<c:url value="/resources/new_message.png"/>' class="new_gen_mes" id="wall_m"></div>
        <div class="tool" id="chat" onclick="location.href = '/${orgUrl}/chat'">Чат
            <img src='<c:url value="/resources/new_message.png"/>' class="new_gen_mes" id="chat_m"></div>
        <div class="tool" id="staff" onclick="location.href = '/${orgUrl}/staff'">Структура</div>
        <div class="tool" id="groups" onclick="location.href = '/${orgUrl}/groups'">Группы</div>
        <div class="tool" id="storage" onclick="location.href = '/${orgUrl}/storage'">Файлообменник</div>
        <div class="tool" id="notes" onclick="location.href = '/${orgUrl}/notes'">Заметки</div>
        <div class="tool" id="calendar" onclick="location.href = '/${orgUrl}/calendar'">Календарь</div>
        <div class="tool" id="apps" onclick="location.href = '/${orgUrl}/apps'">Приложения</div>
        <div class="datepicker-here"></div>
        <div class="log log_message">Это просто такой текст лога</div>


    </div>
    <div class="center">
        <t:insertAttribute name="center"/>
    </div>
    <div class="contacts">
        <iframe src="<c:url value='/resources/static/contacts/contacts.html'/>" frameborder="0" class="contacts-frame">
        </iframe>
    </div>

</div>
</body>
</html>
