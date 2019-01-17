<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<header>
    <link rel="stylesheet" href="<core:url value="/resources/static/index.css"/>">
    <link rel="stylesheet" href="<core:url value="/resources/static/gen.css"/>">
    <link rel="stylesheet" href="<core:url value="/resources/static/tools/tools.css"/>">
    <script src="<core:url value="/resources/libs/jquery_3.1.0.js"/>"></script>
    <script src="<core:url value="/resources/libs/jquery.arcticmodal-0.3/jquery.arcticmodal-0.3.min.js"/>"></script>
    <script src="<core:url value="/resources/libs/ckeditor/ckeditor.js"/>"></script>
    <script type="text/javascript">
        var myTree = null;

        window.listOfDatesNoted = [];
        $(document).ready(function () {

           /* $("#groups").click(function () {
                location.href = "/groups";
            });*/


        });


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

        function getCurrentDate() {
            var now = new Date();
            var options = {
                year: 'numeric',
                month: 'numeric',
                day: 'numeric',
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric'
            };
            return now.toLocaleString('ru', options);
        }


        /*$(function(){
                $('#exampleModal').arcticmodal();

            });*/

    </script>
    <div class="title" align="center">Name</div>
    <div class="user">
        <span class="user_name">Иванов Иван Иванович</span>
        <img src="" class="user_photo">
    </div>
</header>
<body>
<div class="container">
    <div class="tools">
        <div class="tool" id="wall"><a href='/${sessionScope.get("orgUrl")}/wall'>Лента</a></div>
        <div class="tool" id="chat"><a href='/${sessionScope.get("orgUrl")}/chat'>Чат</a></div>
        <div class="tool" id="staff"><a href='/${sessionScope.get("orgUrl")}/staff'>Сотрудники</a></div>
        <div class="tool" id="groups"><a href='/${sessionScope.get("user").groups}/groups'>Группы</a></div>
        <div class="tool" id="storage"><a href='/${sessionScope.get("orgUrl")}/storage'>Хранилище</a></div>
        <div class="tool" id="notes"><a href='/${sessionScope.get("orgUrl")}/notes'>Заметки</a></div>
        <div class="tool" id="calendar"><a href='/${sessionScope.get("orgUrl")}'>Календарь</a></div>
        <div class="tool" id="apps"><a href='/${sessionScope.get("orgUrl")}/apps'>Приложения</a></div>
        <div class="datepicker-here"></div>

    </div>
    <div class="center">
        <t:insertAttribute name="center"/>
    </div>
    <div class="contacts">
        <iframe src="<core:url value='/resources/static/contacts/contacts.html'/>" frameborder="0"></iframe>
    </div>

</div>
</body>
</html>
