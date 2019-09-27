<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Скачать приложение</title>
    <link rel="stylesheet" href="/resources/index/notice.css">
    <script type="text/javascript" src="/resources/libs/jquery-3.4.1.min.js"></script>
    <script type="text/javascript">
        $("#linux").click(function () {
            $.get("/stat/path", {dld: "l"});
        })
    </script>
</head>
<body>
<div class="container">
    <div class="notice">
        <div class="text_wrapper">
            <p>Использование облачного сервера накладывает ограничения на пропускную способность, размер дискового пространства
             и общую производительность. При использовании сервера в локальной сети отпадает необходимость подключения к интернету.
            Однако для развертывания приложения на вашем локальном сервере потребуются навыки администрирования ОС.
            Также в инструкции не оговариваются нюансы настройки базы данных. Если вы уже используете MYSQL или POSTGRESQL
            на вашем сервере, настройка приложения не составит труда. В дальшейшем будет добавлена и инструкция по настройке баз данных.</p>
            <p>Приложение с инструкцией равзертывания доступно по ссылке ниже.</p>
            </div>
    </div>
    <div class="link" id="linux"><a href="https://yadi.sk/d/s_uCYq2rwJBNmg">Скачать для Windows</a></div>
    <div class="link" title="В разработке.">Скачать для Linux</div>
    <div class="a_wr"><a href="/">На главную</a></div>
</div>
</body>
</html>