<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="yandex-verification" content="2ad2b1ce3643b4f4" />
    <meta name="google-site-verification" content="W4OP_qlQRXA_3lflLIfVH-G3EQScfHggF_Mvm_lc0po" />
    <meta name="Description" content="enChat - корпоративный чат, вобравший в себя идеи корпоративных социальных сетей и простоты использования"/>
    <meta name="Keywords" content="корпоративный чат, корпоративная социальная сеть, общение внутри организации, чат сотрудников, enterprise chat, social network, чат на предприятии"/>
    <title>enChat</title>
    <link rel="stylesheet" href="/resources/index/index.css">
    <script type="text/javascript" src="/resources/index/index.js" defer></script>
</head>
<body>
<!--<div id="header"><h1>Корпоративный чат - програмное решение для связи сотрудников в пределах предприятия</h1></div>-->
<picture>
    <source srcset="/resources/data/app/office.webp" type="image/webp">
    <source srcset="/resources/data/app/office.jpg" type="image/jpeg">
    <img src="/resources/data/app/office.jpg" class="office"/>
</picture>

<img src="/resources/data/app/arrow.gif" class="arrow"/>
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

        <picture>
            <source srcset="/resources/data/app/example.webp" type="image/webp">
            <source srcset="/resources/data/app/example.JPG" type="image/jpeg">
            <img src="/resources/data/app/example.JPG" class="example"/>
        </picture>
        <picture>
            <source srcset="/resources/data/app/example2.webp" type="image/webp">
            <source srcset="/resources/data/app/example2.JPG" type="image/jpeg">
            <img src="/resources/data/app/example2.JPG" class="example"/>
        </picture>

       <%-- <img class="example" id="ex1" src="" data-src="/resources/data/app/example.webp">
        <img class="example" id="ex2" src="" data-src="/resources/data/app/example2.webp">--%>
    </div>
    <div class="variants">
        <p>Варианты развертывания:</p>
        <div>
            <div class="wrapper" id="cloud_w"><li class="deployment_type">облачный сервер</li></div>
            <span class="lets_go" id="cloud">Начать использование</span>
        </div>

        <div>
            <div class="wrapper" id="local_w"><li class="deployment_type">ваш локальный сервер</li></div>
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