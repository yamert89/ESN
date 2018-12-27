<%@ taglib prefix="t" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<header>
    <link rel="stylesheet" href="<core:url value="/resources/static/index.css"/>">
    <link rel="stylesheet" href="<core:url value="/resources/static/gen.css"/>">
    <script src="<core:url value="/resources/libs/jquery_3.1.0.js"/>"></script>
    <script src="<core:url value="/resources/libs/jquery.arcticmodal-0.3/jquery.arcticmodal-0.3.min.js"/>"></script>
    <script src="<core:url value="/resources/libs/ckeditor/ckeditor.js"/>"></script>
    <script type="text/javascript">
        var myTree = null;
        var userId = null; //${userId}; //TODO
        window.listOfDatesNoted = [];
        $(document).ready(function () {



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
        <div class="tool">Лента</div>
        <div class="tool">Чат</div>
        <div class="tool">Сотрудники</div>
        <div class="tool">Группы</div>
        <div class="tool">Хранилище</div>
        <div class="tool">Заметки</div>
        <div class="tool">Календарь</div>
        <div class="tool">Приложения</div>
        <div class="datepicker-here"></div>

    </div>
    <div class="center">
        <t:insertAttribute name="center"/>
    </div>
    <div class="contacts">
        <iframe src="resources/static/contacts/contacts.html" frameborder="0"></iframe>
    </div>

</div>
</body>
</html>
