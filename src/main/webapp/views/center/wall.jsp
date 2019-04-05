
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
    <link rel="stylesheet" href="<c:url value="/resources/static/center/wall/wall.css"/>">
    <script type="text/javascript" src="<c:url value="/resources/static/center/wall/wall.js"/>"></script>
    <script type="text/javascript">
        var submit;
        var blocker = false;
        $("#wall").addClass("selected");

        $(document).ready(function () {
            var addButton = $('.post_add').first();
            window.userId = addButton.attr("data-userId");
            window.orgUrl = addButton.attr("data-ogrUrl");
            window.avatar = addButton.attr("data-img");
            window.avatar_small = addButton.attr("data-img-small");
            window.userName = addButton.attr("data-name");
            addButton.click(function () {
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
                var mes = {};
                mes.userName = window.userName;
                mes.imgUrl = window.avatar_small;
                mes.time = window.getDate(new Date());
                mes.text = data;

                renderPost(mes, false);

                /*$(".posts").prepend('<div class="post">' +
                    '<div class="message_info_wrapper">' +
                    '<div class="message_info_w">' +
                    '<img src="' + img + '" class="person_photo_small">' +
                    '<div class="person_name_w">' + name + '</div>' +
                    '<div class="message_time_w">' + time + '</div>' +
                    '</div>' +
                    '</div>' + data + '</div>');*/

                $.ajax({type:"POST", url:"/savepost", data:{"userId":userId, "text":data, "time":mes.time, "orgUrl":orgUrl}})


            });

            $(".posts").scroll(function () {
                if (blocker || $(this)[0].scrollHeight - ($(this)[0].scrollTop + $(this).height()) > 300) return;
                $.get("/wallpiece", {}, function (data) {
                    if (data == null) return;
                    data.forEach(function (el) {
                        renderPost(el, true);
                    });
                    blocker = false;
                    console.log("false")
                }, 'json');
                blocker = true;
                console.log("true")
            });

            $(document).on('click', '.delete_message', function () {
                $(this).parent().parent().remove();
                var text = $(this).parent().parent().children().last().text();
                $.ajax({url : "/deletepost", method : "POST", data :{text: text}})
            })


        });



        function renderPost(mes, after) {
            var visibleCross = after ? 'style=""' :  ' style="display: inline-block";';
            var data = '<div class="post">\n' +
                '            <div class="message_info_wrapper">\n' +
                '                <div class="message_info_w">\n' +
                '                    <img src="/resources/avatars/' + mes.imgUrl + '" class="person_photo_small">\n' +
                '                    <div class="person_name_w">' + mes.userName + '</div>\n' +
                '                    <div class="message_time_w">' + mes.time + '</div>\n' +
                '                </div><img class="delete_message" src="/resources/cross.png"' + visibleCross + '>' +
                '            </div>\n' + mes.text + '</div>';
            if(after) $(".posts").append(data);
            else $(".posts").prepend(data);

        }

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
<div class="post_add_wrapper"><c:set var="user" value="${sessionScope.get('user')}"/>
    <button class="post_add" data-name="${user.name}" data-img="${user.photo}"
            data-userId="${user.id}" data-ogrUrl="${sessionScope.get("orgUrl")}"  data-img-small="${user.photo_small}">Добавить новость</button></div>
<textarea name="editor" class="editor" rows="10" cols="80"></textarea>
<button class="post_submit">Опубликовать новость</button>
<textarea name="sample_editor" class="sample_editor" rows="10" cols="80"></textarea>
<div class="posts">
    <c:forEach var="mes" items="${messages}">
        <div class="post">
            <div class="message_info_wrapper">
                <div class="message_info_w">
                    <img src='<c:url value="/resources/avatars/${mes.imgUrl}"/>' class="person_photo_small">
                    <div class="person_name_w">${mes.userName}</div>
                    <div class="message_time_w"><fmt:formatDate value="${mes.time}" pattern="HH:mm:ss / dd.MM"/></div>
                </div>
            </div>
            ${mes.text}
        </div>
    </c:forEach>

</div>
</body>
</html>
