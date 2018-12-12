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
    <link rel="stylesheet" href="<core:url value="/resources/static/center/wall/wall.css"/>">
    <script type="text/javascript" src="<core:url value="/resources/static/center/wall/wall.js"/>"></script>
</head>
<body>
<div class="post_add_wrapper">
    <button class="post_add">Добавить новость</button></div>
<textarea name="editor" class="editor" rows="10" cols="80"></textarea>
<button class="post_submit">Опубликовать новость</button>
<textarea name="sample_editor" class="sample_editor" rows="10" cols="80"></textarea>
<div class="posts">
    <div class="post">
        <div class="message_info_wrapper">
            <div class="message_info_w">
                <img src="" class="person_photo_small">
                <div class="person_name_w">Булыгина Мария Ивановна</div>
                <div class="message_time_w">10: 30 21.09.2018</div>
            </div>
        </div>
        <p>ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем ПРивет всем </p>
    </div>
</div>
</body>
</html>
