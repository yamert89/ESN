<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<header>
    <link rel="stylesheet" href="<c:url value="/resources/static/index.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/gen.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/tools/tools.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/center/notes/notes.css"/>">
    <script src="<c:url value="/resources/libs/jquery_3.1.0.js"/>"></script>
    <script src="<c:url value="/resources/libs/jquery.arcticmodal-0.3/jquery.arcticmodal-0.3.min.js"/>"></script>
    <script src="<c:url value="/resources/libs/ckeditor/ckeditor.js"/>"></script>
    <script type="text/javascript">
        var myTree = null;

        window.listOfDatesNoted = [];
        $(document).ready(function () {
            var uName = $(".user_name");
            var uTitle = $(".title");

            window.userName = uName.text();
            window.login = uName.attr("data-login");
            window.userId = uName.attr("data-user_id");
            window.orgName = uTitle.text();
            window.orgUrl = uTitle.attr("data-url");


            uName.click(function () {
                props();
            });

            $(".user_photo").click(function () {
                props();
            });



            function props() {
                location.href = "/" + orgUrl + "/users/" + login;
            }


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

        var callFrame = function(link) {
            location.href = link;
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
        <div class="tool" id="notes"><a href='/${orgUrl}}/notes'>Заметки</a></div>
        <div class="tool" id="calendar"><a href='/${orgUrl}'>Календарь</a></div>
        <div class="tool" id="apps"><a href='/${orgUrl}}/apps'>Приложения</a></div>
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
