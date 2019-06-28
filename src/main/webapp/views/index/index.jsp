<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Enterprise software</title>
    <link rel="stylesheet" href="/resources/index/index.css">
    <script type="text/javascript" src="/resources/libs/jquery-3.4.1.min.js"></script>
    <script type="text/javascript">

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
            })



        });
    </script>
</head>
<body>
<!--<div id="header"><h1>Корпоративный чат - програмное решение для связи сотрудников в пределах предприятия</h1></div>-->
<img src="../../resources/data/office.jpg" id="header"/>
<div class="header_text_wrapper">
    <h1>Корпоративный чат - программное решение для связи сотрудников в пределах предприятия</h1>
</div>
<div class="features">
    <p>Чат выполнен как браузерное приложение и прост в использовании.</p>
    <p>Текущая версия - beta</p>
    Основные возможности :
    <li>общий и приватный чат</li>
    <li>стена</li>
    <li>загрузка/скачивание файлов</li>
    <li></li>
</div>
<div class="variants">
    Варианты развертывания:<br/>
    <div class="wrapper"><li class="deployment_type">облачный сервер</li></div><span class="lets_go" id="cloud">Начать использование</span>
    <br/>
    <div class="wrapper"><li class="deployment_type">ваш локальный сервер</li></div><span class="lets_go" id="local">Начать использование</span>
</div>


</body>
</html>