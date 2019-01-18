<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <script type="text/javascript">
        $(document).ready(function () {
            $(".post_add").click(function () {
                var note = prompt("Введите текст заметки", "");
                var time = getCurrentDate();
                $(".notes").prepend("<div class='note'><div class='date'>" + time +"</div><div class='note_text'>" + note + "</div>");
                $.ajax({type:"POST", url:"/savenote", data:{time:time, text:note}});
            });
        });
    </script>
</head>
<body>
<div class="post_add_wrapper">
    <button class="post_add">Добавить заметку</button></div>
<div class="notes">
    <c:forEach var="note" items='${sessionScope.get("user").notes}'>
        <div class="note">
            <div class="date">${note.key}</div>
            <div class="note_text">${note.value}</div>
        </div>
    </c:forEach>






   <%-- <div class="note">
        <div class="date">17.08.2018</div>
        <div class="note_text">Это обычный текст. И еще текст.</div>
    </div>
    <div class="note">
        <div class="date">18.08.2018</div>
        <div class="note_text">Это обычный текст. И еще текст. Это обычный текст. И еще текст. Это обычный текст. И еще текст. Это обычный текст. И еще текст.<br>Это обычный текст. И еще текст.</div>
    </div>--%>
</div>
</body>
</html>
