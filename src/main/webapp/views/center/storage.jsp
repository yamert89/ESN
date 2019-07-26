<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <title>Хранилище</title>
    <link rel="stylesheet" href="<c:url value="/resources/static/center/storage/storage.css"/>">
    <script type="text/javascript" src='<c:url value="/resources/static/center/storage/storage.js"/>'></script>
</head>
<body>

<div class="storage" data-path="${filesPath}">
    <div class="storage_public">

        <div class="storage_header">Общие файлы</div>
        <div class="storage_wrapper" id="shared_files">
            <svg width="100%" height="100%" viewBox="0 0 42 42" class="donut"  id="publicDiagram">
                <circle class="donut-hole" cx="21" cy="21" r="15.91549430918954" fill="#fff"></circle>
                <circle class="donut-ring" cx="21" cy="21" r="15.91549430918954" fill="transparent" stroke="#d2d3d4" stroke-width="5"></circle>
                <circle class="donut-segment" cx="21" cy="21" r="15.91549430918954" fill="transparent" stroke="#000000" stroke-width="5" stroke-dasharray="0 100" stroke-dashoffset="0"></circle>
                <text x="50%" y="50%" class="diagram_text">0%</text>
            </svg>
            <c:forEach var="file" items='${sharedFiles}'>
                <div class="file">
                    <img src="" class="file_ico" data-ext="${file.extension}" title="Скачать">
                    <input class="fileName" readonly title="${file.name}" value="${file.name}">
                    <div class="file_author"><a href='/${sessionScope.get("org").getUrlName()}/users/${file.owner.login}'>${file.owner.name}</a></div>
                    <div class="file_time">${file.time}</div>
                </div>
            </c:forEach>


        </div>
        <form action="" class="form_file" method="post" enctype="multipart/form-data">
            <input type="file" name="file" data-shared="1" class="file_input">
            <%--<input type="text" name="fileName" placeholder="Новое имя">--%>
            <input type="button" value="Загрузить" class="btn_load_file">
        </form>
    </div>
    <div class="storage_private">

        <div class="storage_header">Личные файлы</div>
        <div class="storage_wrapper" id="private_files">
            <svg width="100%" height="100%" viewBox="0 0 42 42" class="donut"  id="privateDiagram">
                <circle class="donut-hole" cx="21" cy="21" r="15.91549430918954" fill="#fff"></circle>
                <circle class="donut-ring" cx="21" cy="21" r="15.91549430918954" fill="transparent" stroke="#d2d3d4" stroke-width="5"></circle>
                <circle class="donut-segment" cx="21" cy="21" r="15.91549430918954" fill="transparent" stroke="#000000" stroke-width="5" stroke-dasharray="0 100" stroke-dashoffset="0"></circle>
                <text x="50%" y="50%" class="diagram_text">0%</text>
            </svg>
            <c:set var="files" value='${user.storedFiles}'/>
            <c:forEach var="file" items='${files}'>
                <div class="file">
                    <img src="" class="file_ico" data-ext="${file.extension}"  title="Скачать">
                    <input class="fileName" type="text" title="${file.name}" value="${file.name}" onchange="rename(this)">
                    <img src='<c:url value="/resources/cross.png"/>' class="file_delete" title="Удалить">
                    <c:if test="${file.shared == false}">
                        <img src='<c:url value="/resources/share.png"/>' data-shared='0' class="file_share" title="Опубликовать в общие">
                    </c:if>
                    <c:if test="${file.shared == true}">
                        <img src='<c:url value="/resources/unshare.png"/>' data-shared='1' class="file_share" title="Убрать из общих">
                    </c:if>
                    <div class="file_time">${file.time}</div>
                </div>
            </c:forEach>

        </div>
        <form action="" class="form_file" method="post" enctype="multipart/form-data">
            <input type="file" name="file" data-shared="0" class="file_input">
            <%--<input type="text" name="fileName" placeholder="Новое имя">--%>
            <input type="button" value="Загрузить" class="btn_load_file">
        </form>
    </div>
</div>
</body>
</html>


