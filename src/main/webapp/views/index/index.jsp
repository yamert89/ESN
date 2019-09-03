<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="Description" content="enChat - корпоративный чат, вобравший в себя идеи корпоративных социальных сетей и простоты использования"/>
    <meta name="Keywords" content="корпоративный чат, корпоративная социальная сеть, общение внутри организации, чат сотрудников, enterprise chat, social network, чат на предприятии"/>
    <title>enChat</title>
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
            var ex1 = $("#ex1");
            var ex2 = $("#ex2");
            ex1.attr("src", ex1.attr("data-src"));
            ex2.attr("src", ex2.attr("data-src"));
            arrow = $(".arrow");

             setTimeout(function () {
                $("body").css("overflow-y", "auto");
                if (window.pageYOffset > 0) return;
                arrow.attr("src", arrow.attr("data-src"));
                arrow.css("display", "block");
            }, 2000);
            arrow.click(scroll);
            $(window).scroll(scroll);

            $(".q").click(function () {
                var question = $(this).next();
                if (question.hasClass("hidden")) {
                    showTree(question);
                    $(document).scrollTop(9999);
                }
                else hideTree(question);
            });

            $(".send").click(function () {
                var email = $(".email").val();
                var text = $("textarea").val();
                $.post("/question", {message : text, email : email});
                hideTree($(".question"));
            })



        });

        function showTree(element) {
            element.removeClass("hidden");
            var children = element.children();
            if (children.length > 0) showTree(children);

        }

        function hideTree(element) {
            element.addClass("hidden");
            var children = element.children();
            if (children.length > 0) hideTree(children);
        }

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
<img src="../../resources/data/app/office.jpg" id="header"/>
<img src="" data-src="/resources/data/app/arrow.gif" class="arrow"/>
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

        <img class="example" id="ex1" src="" data-src="/resources/data/app/example.JPG">
        <img class="example" id="ex2" src="" data-src="/resources/data/app/example2.JPG">
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

    <p class="q">Задать вопрос</p>
    <div class="question hidden">
        <textarea name="" id="" cols="30" rows="10" class="hidden"></textarea>
        <div class="email_wr hidden"><span class="email_label hidden">Ваша почта для обратной связи:</span><input type="text" class="email hidden"></div>
        <button class="send hidden">Отправить</button>
    </div>

</div>


</body>
</html>