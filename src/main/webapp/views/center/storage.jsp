<%@ taglib prefix="core" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 29.11.2018
  Time: 10:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="<core:url value="/resources/static/center/storage/storage.css"/>">
</head>
<body>
<div class="storage">
    <div class="storage_public">

        <div class="storage_header">Общие файлы</div>
        <div class="storage_wrapper">
            <div class="file f_doc">
                <img src="resources/icons/word.png" class="file_ico">
                <div class="fileName">Имя файла</div>
                <div class="file_author">Петя Васечкин</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico">
                <span class="fileName">Имя файла</span>
                <div class="file_author">Петя Васечкин</div>
            </div>
        </div>
        <form action="">
            <input type="file" value="Загрузить" class="btn_load_file">
        </form>
    </div>
    <div class="storage_private">

        <div class="storage_header">Личные файлы</div>
        <div class="storage_wrapper">
            <div class="file f_doc">
                <img src="resources/icons/word.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>
            <div class="file f_audio">
                <img src="resources/icons/music.png" class="file_ico"><div class="fileName">Имя файла</div>
            </div>

        </div>
        <form action="">
            <input type="file" value="Загрузить" class="btn_load_file">
        </form>
    </div>
</div>
</body>
</html>
