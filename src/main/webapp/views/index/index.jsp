<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Enterprise software</title>
    <link rel="stylesheet" href="/resources/index/index.css">
    <script type="text/javascript" src="/resources/libs/jquery-3.4.1.min.js"></script>
    <script type="text/javascript">
        var arrow = {};

        $(document).ready(function () {
            $(".wrapper").hover(function () {
                var showed = $(".lets_go_showed");
                showed.removeClass("lets_go_showed");
                showed.addClass("lets_go");
                var go = $(this).next();
                go.removeClass("lets_go");
                go.addClass("lets_go_showed");
            });

            $(".lets_go").click(function () {
                location.href = $(this).attr("id") === "cloud" ? "/notice_cloud" : "/notice_local";
            });
            var example = $(".example");
            example.attr("src", example.attr("data-src"));
            arrow = $(".arrow");
            setTimeout(function () {
                arrow.attr("src", arrow.attr("data-src"));
                arrow.css("display", "block");
                $("body").css("overflow-y", "auto");
            }, 2000);
            arrow.click(scroll);
            $(window).scroll(scroll);



        });

        function scroll() {
            $(window).unbind("scroll", scroll);
            //window.scrollTo(0, $("#header").height() + 2);
            $('html, body').animate({ scrollTop: $("#header").height() + 2 }, 600);
            arrow.css("display", "none");
        }
    </script>
</head>
<body>
<!--<div id="header"><h1>Корпоративный чат - програмное решение для связи сотрудников в пределах предприятия</h1></div>-->
<img src="../../resources/data/office.jpg" id="header"/>
<img src="" data-src="/resources/data/arrow.gif" class="arrow"/>
<div class="main_wrapper">
    <div class="header_text_wrapper">
        <h2><span class="name">enChat</span> - программное решение для связи сотрудников в пределах предприятия</h2>
    </div>

    <div class="features">

        <p>Чат выполнен как браузерное приложение и прост в использовании.</p>

        <p>Основные возможности :</p>

        <div class="features_li">
            <li>общий и приватный чат</li>
            <li>стена</li>
            <li>загрузка / скачивание файлов</li>
            <li>производственный календарь</li>
        </div>

        <img class="example" src="" data-src="/resources/data/example.JPG">
    </div>
    <div class="variants">
        <p>Варианты развертывания:</p>
        <div>
            <div class="wrapper"><li class="deployment_type">облачный сервер</li></div>
            <span class="lets_go" id="cloud">Начать использование</span>
        </div>

        <div>
            <div class="wrapper"><li class="deployment_type">ваш локальный сервер</li></div>
            <span class="lets_go" id="local">Начать использование</span>
        </div>

    </div>

</div>


</body>
</html>