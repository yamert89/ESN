<%@ page import="java.util.Date" %>
<%@ page import="esn.entities.User" %>
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
            $("#notes").addClass("selected");
            $(".post_add").click(function () {
                var note = prompt("Введите текст заметки", "");
                if (note == null) return;
                var time = getDate(new Date());
                $(".notes").prepend("<div class='note'><div class='date'>" + time +"</div><div class='note_text'>" + note + "</div>");
                $.ajax({type:"POST", url:"/note", data:{time:time, text:note}});
            });
        });
    </script>
</head>
<body>
<div class="post_add_wrapper">
    <button class="post_add post_submit">Добавить заметку</button></div>
<div class="notes"><%System.out.println(new Date());
System.out.println(((User) session.getAttribute("user")).getNotes());%>
    <c:forEach var="note" items='${sessionScope.get("user").notes}'>
        <div class="note">
            <div class="date">${note.key}</div>
            <div class="note_text">${note.value}</div>
        </div>
    </c:forEach>
</div>
</body>
</html>
