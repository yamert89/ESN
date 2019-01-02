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
    <script type="text/javascript">
        var submit;
        $(document).ready(function () {
            $('.post_add').click(function () {
                showEditor();
            });
            submit = $(".post_submit");
            submit.click(function () {
                var data;
                try{
                    data = CKEDITOR.instances.editor.getData();
                }catch (e) {
                    return;
                }

                hideEditor();

                var NAME = ''; //TODO server
                var IMG = ''; //TODO server
                var time = window.getCurrentDate();

                $(".posts").prepend('<div class="post">' +
                    '<div class="message_info_wrapper">' +
                    '<div class="message_info_w">' +
                    '<img src="' + IMG + '" class="person_photo_small">' +
                    '<div class="person_name_w">' + NAME + '</div>' +
                    '<div class="message_time_w">' + time + '</div>' +
                    '</div>' +
                    '</div>' + data + '</div>');

                $.ajax({type:"POST", url:"/savepost", data:{"userId":userId, "text":data, "time":time, "orgUrl":orgUrl}})


            });
        });

        function showEditor() {
            try {
                CKEDITOR.replace('editor');
                /*editor.config.autoParagraph = false;*/
                CKEDITOR.instances.editor.setData('<p></p>');
            } catch (e) {
               alert("Ошибка выполнения скрипта");
            }

            $(".post_add").css("display", "none");
            submit.css({"display": "inline"});

        }

        function hideEditor() {
            $("#cke_editor").replaceWith($('.sample_editor'));
            submit.css("display", "none");
            $(".post_add").css("display", "inline");
            CKEDITOR.instances.editor.destroy();

        }

        /*<div class="message_info_wrapper">
            <div class="message_info_w">
            <img src="" class="person_photo_small">
            <div class="person_name_w">Булыгина Мария Ивановна</div>
        <div class="message_time_w">10: 30 21.09.2018</div>
        </div>
        </div>*/

    </script>
</head>
<body>
<div class="post_add_wrapper">
    <button class="post_add" data-name="" data-img="">Добавить новость</button></div>
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
