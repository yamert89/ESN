<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Created by IntelliJ IDEA.
  User: porohin
  Date: 29.11.2018
  Time: 10:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="<c:url value="/resources/static/center/chat/chat.css"/>">
    <link rel="stylesheet" href="<c:url value="/resources/static/index.css"/>">
    <script type="text/javascript">
        $(document).ready(function () {
            var date = new Date();
            console.log('private ready ' + new Date().getSeconds() + ':' + date.getMilliseconds());
            setTimeout(selectCompan, 50);


            function selectCompan(){
                var date = new Date();
                console.log('func start ' + new Date().getSeconds() + ':' + date.getMilliseconds());
                var companId = $('.person_container').attr('data-companion-id');
                var currentCompanion = $('.contacts-frame').contents().find('[data-id=' + companId + ']');
                if(currentCompanion.length < 1) {
                    setTimeout(selectCompan, 50);
                    return;
                }
                currentCompanion.addClass("selected");
            }




            var sizeMes = 800;
            var textField = $(".new_genchat_message");
            var messBtn = $(".new_genchat_message_btn");
            messBtn.click(function () {
                var text = textField.val();
                if (text == "") return;
                var comp_id = $(".person_container").attr('data-companion-id');
                var time = window.getDate(new Date());
                if (text.length > sizeMes) notify('Максимальный размер сообщения ' + sizeMes +' символов. Оно будет разбито.');
                $(".private_chat_container").prepend('<div class="private_chat comment_bubble_right unreaded"><div class="time-right">' + time + '</div>' + text + '</div>');
                $.ajax({type:"POST", url:"/save_private_message/" + comp_id, data:{"text":text}});
                textField.val('');
            });
            $(document).keypress(function (event) {
                if (event.which == 13) messBtn.click();
            });
            var comp_login = $(".person_container").attr('data-companion-login');
            $(".person_photo_chat").click(function () {
                location.href = "/" + orgUrl + "/users/" + comp_login;
            })

        });
    </script>
</head>
<body>
<div class="private_chat_wrapper">
<div class="private_chat_profile">
    <div class="person_container" data-companion-id="${companion.id}" data-companion-login="${companion.login}">
        <img src='<c:url value="/resources/data${companion.photo}"/>' class="person_photo_chat"><br>
        <c:if test="${!companion.netStatus}">
            <div class="net_status_circle" id="net_status_off"></div>
            <span class="net_status">не в сети</span>
        </c:if>
        <c:if test="${companion.netStatus}">
            <div class="net_status_circle"></div>
            <span class="net_status">в сети</span>
        </c:if>
    </div>
    <span class="person_name_chat">${companion.name}</span><br>
    <div class="new_message_container">
        <input type="text" placeholder="Добавить сообщение" class="new_genchat_message flex">
        <button class="new_genchat_message_btn flex">Отправить</button>
    </div>
</div>
<div class="private_chat_container">
    <c:if test="${messages != null}">
        <c:forEach var="mes" items="${messages}">
        <c:choose>
        <c:when test="${mes.value && mes.key.readed}">
        <div class="private_chat comment_bubble_right"><div class="time-right">
            </c:when>
            <c:when test="${!mes.value && mes.key.readed}">
            <div class="private_chat comment_bubble_left"><div class="time-left">
                </c:when>
                <c:when test="${mes.value && !mes.key.readed}">
                <div class="private_chat comment_bubble_right unreaded"><div class="time-right">
                    </c:when>
                    </c:choose>
                    <fmt:formatDate value="${mes.key.time.time}" pattern="HH:mm:ss / dd.MM"/>
                </div>${mes.key.text}
                </div>
                </c:forEach>
    </c:if>

</div></div>
</body>
</html>
