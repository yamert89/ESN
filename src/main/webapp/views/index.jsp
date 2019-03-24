<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<header>
    <link rel="stylesheet" href="<c:url value="/resources/static/index.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/gen.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/tools/tools.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/center/notes/notes.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/libs/air-datepicker-master/dist/css/datepicker.css"/>">
    <script src="<c:url value="/resources/libs/jquery_3.1.0.js"/>"></script>
    <script src="<c:url value="/resources/libs/air-datepicker-master/dist/js/datepicker.js"/>"></script>

    <script src="<c:url value="/resources/libs/ckeditor/ckeditor.js"/>"></script>
    <script type="text/javascript">
        var myTree = null;
        function DayWithText(day){
            this.day = day;
            this.notes = [];
        }

        window.listOfDatesNoted = [];
        $(document).ready(function () {
            var uName = $(".user_name");
            var uTitle = $(".title");

            window.userName = uName.text();
            window.login = uName.attr("data-login");
            window.userId = uName.attr("data-user_id");
            window.orgName = uTitle.text();
            window.orgUrl = uTitle.attr("data-url");
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
                        /*if (!window.inited){
                            window.inited = true;
                            return;
                        }*/
                       /* if (new Date().getFullYear() != year) return;*/ //TODO

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

                        $.ajax({type:"POST", url:"/note", data:{time:getDate(date), text:note, success: function () {
                                    location.reload();
                                }}});
                        //TODO


                        // Если выбрана дата с событием, то отображаем его
                        /*if (date && eventDates.indexOf(date.getDate()) != -1) {

                        }*/

                    },
                    onChangeMonth: function (monthN, year) {
                        if (new Date().getFullYear() != year) return; //ToDO TEst
                        window.eventDates = getEventDates(monthN);
                        var pickerObj = $picker.data('datepicker');
                        pickerObj.update('minDate');
                    },
                    moveToOtherMonthsOnSelect:false
                });


// Сразу выберем какую-ниудь дату из `eventDates`
                /*var currentDate = currentDate = new Date();
                $picker.data('datepicker').selectDate(new Date(currentDate.getFullYear(), currentDate.getMonth(), 10))*/
            }



           /* $("#groups").click(function () {
                location.href = "/groups";
            });*/


        });

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
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric'
            };
            return date.toLocaleString('ru', options);
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


        /*$(function(){
                $('#exampleModal').arcticmodal();

            });*/

    </script><c:set var="user" value='${sessionScope.get("user")}'/>
    <div class="title" align="center" data-url='${sessionScope.get("orgUrl")}'>${user.organization.name}</div>
    <div class="user">
        <span class="user_name" data-login="${user.login}" data-user_id="${user.id}">${user.name}</span>
        <img src='<c:url value="/resources/avatars/${user.photo}"/>' class="user_photo">
    </div>
</header>
<body>
<div class="container">
    <div class="tools"><c:set var="orgUrl" value='${sessionScope.get("orgUrl")}'/>
        <div class="tool" id="wall"><a href='/${orgUrl}/wall'>Лента</a></div>
        <div class="tool" id="chat"><a href='/${orgUrl}/chat'>Чат</a></div>
        <div class="tool" id="staff"><a href='/${orgUrl}/staff'>Структура</a></div>
        <div class="tool" id="groups"><a href='/${orgUrl}/groups'>Группы</a></div>
        <div class="tool" id="storage"><a href='/${orgUrl}/storage'>Файлообменник</a></div>
        <div class="tool" id="notes"><a href='/${orgUrl}/notes'>Заметки</a></div>
        <div class="tool" id="calendar"><a href='/${orgUrl}'>Календарь</a></div>
        <div class="tool" id="apps"><a href='/${orgUrl}/apps'>Приложения</a></div>
        <div class="datepicker-here"></div>


    </div>
    <div class="center">
        <t:insertAttribute name="center"/>
    </div>
    <div class="contacts">
        <iframe src="<c:url value='/resources/static/contacts/contacts.html'/>" frameborder="0"></iframe>
    </div>

</div>
</body>
</html>
